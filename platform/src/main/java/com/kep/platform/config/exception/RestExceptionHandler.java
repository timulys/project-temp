package com.kep.platform.config.exception;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.exception.BizException;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * {@link RestController} 예외 처리
 */
@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 권한 없는 요청시 예외 처리
     */
    @ExceptionHandler({
            AccessDeniedException.class,
            AuthenticationException.class,
            SecurityException.class
    })
    @Nullable
    protected ResponseEntity<Object> handleAccessDeniedRequest(Exception ex, WebRequest request) {

        log.error("AccessDenied Exception Handler: {}", ex.getLocalizedMessage());
        return handleExceptionInternal(ex, ex.getCause(), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    /**
     * 잘못된 요청시 예외 처리
     */
    @ExceptionHandler({
            MissingRequestHeaderException.class,
            MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    @Nullable
    protected ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {

        log.error("BadRequest Exception Handler: {}", ex.getLocalizedMessage());
        return handleExceptionInternal(ex, ex.getCause(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * 미구현 요청시 예외 처리
     */
    @ExceptionHandler({
            UnsupportedOperationException.class
    })
    @Nullable
    protected ResponseEntity<Object> handleNotImplement(RuntimeException ex, WebRequest request) {

        log.error("NotImplement Exception Handler: {}", ex.getLocalizedMessage());
        return handleExceptionInternal(ex, ex.getCause(), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED, request);
    }

    /**
     * BizException
     */
    @ExceptionHandler({
            BizException.class
    })
    @Nullable
    protected ResponseEntity<Object> handleDefault(BizException ex, WebRequest request) {

        log.error("Biz Exception Handler: {}", ex.getLocalizedMessage());
        return handleExceptionInternal(ex, ex.getCause(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * 그 외 모든 예외 처리
     */
    @ExceptionHandler({
            Exception.class
    })
    @Nullable
    protected ResponseEntity<Object> handleDefault(Exception ex, WebRequest request) {

        log.error("Default Exception Handler: {}", ex.getLocalizedMessage());
        return handleExceptionInternal(ex, ex.getCause(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * {@link ApiResult}로 결과 포맷 정규화
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ServletWebRequest httpServletRequest = (ServletWebRequest) request;
        String requestPath = httpServletRequest.getRequest().getRequestURI();
        String requestQuery = httpServletRequest.getRequest().getQueryString();
        if (!ObjectUtils.isEmpty(requestQuery)) {
            requestPath += "?" + requestQuery;
        }

        Iterator<String> headerIterator = httpServletRequest.getHeaderNames();
        List<String> requestHeaders = new ArrayList<>();
        while (headerIterator.hasNext()) {
            String headerName = headerIterator.next();
            requestHeaders.add(headerName + ": " + httpServletRequest.getHeader(headerName));
        }

        String requestBody = "";
        try {
            requestBody = IOUtils.toString(httpServletRequest.getRequest().getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }

        log.error("RestController Exception, PATH: {}, HEADERS: {}, BODY: {}, {}", requestPath, requestHeaders, requestBody, ex.getLocalizedMessage());
        log.error("RestController Exception", ex);

        // 정규화 가능한 메세지 처리
        String message = "";
        Map<String, Object> extra = null;
        if (ex instanceof UnsupportedOperationException) {
            if (!ObjectUtils.isEmpty(ex.getLocalizedMessage())) {
                message = ex.getLocalizedMessage();
            } else {
                message = "EX-NOP";
            }
        } else if (ex instanceof BizException) {
            if (!ObjectUtils.isEmpty(ex.getLocalizedMessage())) {
                message = ex.getLocalizedMessage();
                extra = ((BizException) ex).getExtra();
            } else {
                message = "EX-NOP";
            }
        }

        ApiResult<String> result = ApiResult.<String>builder()
                .code(ApiResultCode.failed)
                .message(message)
                .extra(extra)
                .build();
        result.setError(ex.getLocalizedMessage());

        return super.handleExceptionInternal(ex, result, headers, status, request);
    }
}
