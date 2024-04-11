package com.kep.portal.config.exception;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * 전역 예외 처리 (alternative BasicErrorController)
 */
@RestController
@Slf4j
public class CustomErrorController implements ErrorController {

	@RequestMapping("/error")
	public ResponseEntity<ApiResult<String>> error(Exception e, HttpServletRequest request) {

		log.error(e.getLocalizedMessage(), e);

		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		int statusCode = 500;
		if (status != null) {
			statusCode = (int) status;
		}
		HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

		ApiResult<String> apiResult = ApiResult.<String>builder()
				.code(ApiResultCode.failed)
				.build();

		return new ResponseEntity<>(apiResult, httpStatus);
	}
}
