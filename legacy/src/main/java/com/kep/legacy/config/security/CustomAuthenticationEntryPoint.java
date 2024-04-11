package com.kep.legacy.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 인증 예외 (AuthenticationException) 처리
 */
@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Resource
	private ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {

		log.error(authenticationException.getLocalizedMessage());

		ApiResult<String> apiResult = ApiResult.<String>builder()
				.code(ApiResultCode.failed)
				.message(authenticationException.getLocalizedMessage())
//				.message(AuthenticationException.class.getSimpleName())
				.build();
		apiResult.setError(authenticationException.getLocalizedMessage());

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		String json = objectMapper.writeValueAsString(apiResult);
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
	}
}
