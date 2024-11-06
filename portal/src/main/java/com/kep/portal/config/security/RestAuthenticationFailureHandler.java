package com.kep.portal.config.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.config.property.SystemMessageProperty;

import lombok.extern.slf4j.Slf4j;

/**
 * 로그인 프로세스 실패시 처리
 */
@Component
@Slf4j
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Resource
	private ObjectMapper objectMapper;

	@Resource
	private SystemMessageProperty systemMessageProperty;


	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

		/*
		 * [2023.04.03 / asher.shin / 로그인 실패 시 에러 문구 전달 START]
		 */
		Map<String,Object> map = new HashMap<String,Object>();
		if(e instanceof BadCredentialsException){
			map.put("message",systemMessageProperty.getPortal().getLogin().getLoginFailedMessage().getBadCredentials());

		} else if(e instanceof DisabledException){
			map.put("message",systemMessageProperty.getPortal().getLogin().getLoginFailedMessage().getDisabled());
			map.put("isLocked",true);

			/*
			 * [2023.04.03 / asher.shin / 로그인 실패 시 에러 문구 전달 END]
			 */
		//2023.1123/ YO / 동시 세션 초과 에러 처리 문구 추가
		} else if (e instanceof SessionAuthenticationException) {
			map.put("message", systemMessageProperty.getPortal().getLogin().getLoginFailedMessage().getAlreadyAccountLoggedIn());
		}

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

		// update : response 변경 요청으로 수정
		String json = objectMapper.writeValueAsString(map);
		response.getWriter().write(json);
		/*
		ApiResult<Map<String,Object>> result = ApiResult.<Map<String,Object>>builder()
				.code(ApiResultCode.failed)
				.payload(map)
				.build();
		result.setError(e.getLocalizedMessage());

		objectMapper.writeValue(response.getWriter().write(json), result);
		 */
	}
}
