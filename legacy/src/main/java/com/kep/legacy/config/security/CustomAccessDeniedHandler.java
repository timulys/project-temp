package com.kep.legacy.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 인가 (AccessDeniedException) 예외 처리
 */
@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Resource
	private ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

		log.error(accessDeniedException.getLocalizedMessage());

		ApiResult<String> apiResult = ApiResult.<String>builder()
				.code(ApiResultCode.failed)
				.message(accessDeniedException.getLocalizedMessage())
//				.message(AccessDeniedException.class.getSimpleName())
				.build();
		apiResult.setError(accessDeniedException.getLocalizedMessage());

		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		String json = objectMapper.writeValueAsString(apiResult);
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
	}
}
