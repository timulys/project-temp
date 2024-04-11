package com.kep.legacy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mock")
@Slf4j
public class MockController {

	@Value("${server.port}")
	private int port;
	@Value("${spring.application.name}")
	private String applicationName;
	@Resource
	private ObjectMapper objectMapper;

	@GetMapping(value = "/ping")
	public ResponseEntity<ApiResult<Map<String, Object>>> ping(
			@RequestHeader HttpHeaders httpHeaders,
			@CookieValue("JSESSIONID") String jSessionId,
			HttpSession httpSession,
			Authentication authentication) {

		log.info("MOCK, PING, JSESSIONID: {}, SESSION: {}, INTERVAL: {}",
				jSessionId, httpSession.getId(), httpSession.getMaxInactiveInterval());
		if (authentication != null) {
			log.info("MOCK, PING, AUTHORITIES: {}", authentication.getAuthorities());
		}

		for (String key : httpHeaders.keySet()) {
			log.info("MOCK, PING, HEADER: {}: {}", key, httpHeaders.get(key));
		}

		return new ResponseEntity<>(new ApiResult<>(ApiResultCode.succeed), HttpStatus.OK);
	}

	@GetMapping(value = "/echo")
//	@PreAuthorize("hasAnyAuthority('READ_ISSUE')")
	public ResponseEntity<ApiResult<Map<String, Object>>> get(
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(required = false) Map<String, String> requestParams,
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpSession httpSession) {

		log.info("MOCK, GET ECHO, JSESSIONID: {}, SESSION: {}, HITS: {}",
				jSessionId, httpSession.getId(), httpSession.getAttribute("hits"));

		Integer hits = (Integer) httpSession.getAttribute("hits");
		if (hits == null) {
			hits = 0;
		}
		httpSession.setAttribute("hits", ++hits);

		for (String key : httpHeaders.keySet()) {
			log.info("MOCK, GET ECHO, HEADER: {}: {}", key, httpHeaders.get(key));
		}
		for (String key : requestParams.keySet()) {
			log.info("MOCK, GET ECHO, PARAM: {}: {}", key, requestParams.get(key));
		}

		Map<String, Object> response = new HashMap<>(requestParams);
		setDefaultResponse(response, hits);
		ApiResult<Map<String, Object>> apiResult = ApiResult.<Map<String, Object>>builder()
				.code(ApiResultCode.succeed)
				.payload(response).build();

		return new ResponseEntity<>(apiResult, HttpStatus.OK);
	}

	@PostMapping(value = "/echo")
//	@CrossOrigin
//	@PreAuthorize("hasAnyAuthority('WRITE_ISSUE')")
	public ResponseEntity<ApiResult<Map<String, Object>>> post(
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam Map<String, String> requestParams,
			@RequestBody(required = false) String requestBody,
			@CookieValue(value = "JSESSIONID", required = false) String jSessionId,
			HttpSession httpSession) throws Exception {

		log.info("MOCK, POST ECHO, SESSION: {}, JSESSIONID: {}, HITS: {}",
				httpSession.getId(), jSessionId, httpSession.getAttribute("hits"));

		Integer hits = (Integer) httpSession.getAttribute("hits");
		if (hits == null) {
			hits = 0;
		}
		httpSession.setAttribute("hits", ++hits);

		for (String key : httpHeaders.keySet()) {
			log.info("MOCK, POST ECHO, HEADER: {}: {}", key, httpHeaders.get(key));
		}
		for (String key : requestParams.keySet()) {
			log.info("MOCK, POST ECHO, PARAM: {}: {}", key, requestParams.get(key));
		}
		for (String key : Collections.list(httpSession.getAttributeNames())) {
			if (key.endsWith("TOKEN")) {
				DefaultCsrfToken token = (DefaultCsrfToken) httpSession.getAttribute(key);
				log.info("MOCK, POST ECHO, CSRF: {}: {}: {}", token.getHeaderName(), token.getParameterName(), token.getToken());
				requestParams.put("token", token.getToken());
			} else {
				log.info("MOCK, POST ECHO, SESSION: {}: {}", key, httpSession.getAttribute(key));
			}
		}
		log.info("MOCK, POST ECHO, BODY: {}", requestBody);

		// TODO: global
		try {
			Map<String, Object> response = objectMapper.readValue(requestBody, new TypeReference<Map<String, Object>>() {});
			for (String key : response.keySet()) {
				log.info("MOCK, POST ECHO, BODY: {}: {}", key, response.get(key));
			}

			response.putAll(requestParams);
			setDefaultResponse(response, hits);
			ApiResult<Map<String, Object>> apiResult = ApiResult.<Map<String, Object>>builder()
					.code(ApiResultCode.succeed)
					.payload(response).build();
			return new ResponseEntity<>(apiResult, null, HttpStatus.ACCEPTED);
		} catch (JsonProcessingException e) {
			log.error(e.getLocalizedMessage());
			log.error(e.getOriginalMessage());
			throw new IllegalArgumentException("BLAH BLAH");
		}
	}

	private void setDefaultResponse(Map<String, Object> response, Integer hits) {

		response.put("service", applicationName);
		response.put("port", port);
		response.put("hits", hits);
		response.put("datetime", ZonedDateTime.now());
	}
}
