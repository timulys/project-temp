package com.kep.portal.config.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * {@link Filter} 예외 처리
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	public ExceptionHandlerFilter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, e);
		}
	}

	public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable e) throws IOException {

		ApiResult<String> apiResult = ApiResult.<String>builder()
				.code(ApiResultCode.failed)
				.build();

		response.setStatus(status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		String json = objectMapper.writeValueAsString(apiResult);
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
	}
}
