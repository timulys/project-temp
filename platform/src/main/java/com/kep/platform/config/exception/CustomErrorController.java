package com.kep.platform.config.exception;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * 전역 예외 처리 (alternative for {@link BasicErrorController})
 */
@RestController
@Slf4j
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public ResponseEntity<ApiResult<String>> error(Exception e, HttpServletRequest request) {
        String errorMessage = extractErrorMessage(e);
        HttpStatus httpStatus = extractHttpStatus(request);
        ApiResult<String> apiResult = buildApiResult(errorMessage);
        return new ResponseEntity<>(apiResult, httpStatus);
    }

    private String extractErrorMessage(Exception e) {
        String errorMessage;
        if (e != null) {
            log.error("Error occurred: " + e.getMessage(), e);
            errorMessage = e.getMessage();
        } else {
            log.error("Error occurred without exception information");
            errorMessage = "Internal Server Error";
        }
        return errorMessage;
    }

    private HttpStatus extractHttpStatus(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = (status != null) ? (int) status : HttpStatus.INTERNAL_SERVER_ERROR.value();
        return HttpStatus.valueOf(statusCode);
    }

    private ApiResult<String> buildApiResult(String errorMessage) {
        return ApiResult.<String>builder()
                .code(ApiResultCode.failed)
                .message(errorMessage)
                .build();
    }
}