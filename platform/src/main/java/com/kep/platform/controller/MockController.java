package com.kep.platform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.event.PlatformEventType;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.platform.config.property.PlatformProperty;
import com.kep.platform.service.kakao.sync.KakaoSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 테스트
 */
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
	@Resource
	private KakaoSyncService kakaoSyncService;

	@GetMapping(value = "/echo")
	public ResponseEntity<ApiResult<Map<String, Object>>> get(
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(required = false) Map<String, String> requestParams,
			HttpSession httpSession,
			Authentication authentication) throws Exception {

		log.info("MOCK, GET ECHO");

		String code = requestParams.get("code");
		if (!ObjectUtils.isEmpty(code)) {
			kakaoSyncService.authorized(code, requestParams);
		}

		for (String key : httpHeaders.keySet()) {
			log.info("MOCK, GET ECHO, HEADER: {}: {}", key, httpHeaders.get(key));
		}
		for (String key : requestParams.keySet()) {
			log.info("MOCK, GET ECHO, PARAM: {}: {}", key, requestParams.get(key));
		}

		Map<String, Object> response = new HashMap<>(requestParams);

		setDefaultResponse(response, null, authentication);
		ApiResult<Map<String, Object>> apiResult = ApiResult.<Map<String, Object>>builder()
				.code(ApiResultCode.succeed)
				.payload(response).build();

		return new ResponseEntity<>(apiResult, HttpStatus.OK);
	}

	@PostMapping(value = "/echo")
	public ResponseEntity<ApiResult<Map<String, Object>>> post(
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam Map<String, String> requestParams,
			@RequestBody(required = false) String requestBody,
			HttpSession httpSession,
			Authentication authentication) throws Exception {

		log.info("MOCK, POST ECHO, SESSION: {}, HITS: {}", httpSession.getId(), httpSession.getAttribute("hits"));

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
		log.info("MOCK, POST ECHO, BODY: {}", requestBody);

		try {
			Map<String, Object> response = new HashMap<>();
			if (!ObjectUtils.isEmpty(requestBody)) {
				response.putAll(objectMapper.readValue(requestBody, new TypeReference<Map<String, Object>>() {}));
				for (String key : response.keySet()) {
					log.info("MOCK, POST ECHO, BODY: {}: {}", key, response.get(key));
				}
			}

			response.putAll(requestParams);
			setDefaultResponse(response, hits, authentication);
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

	private void setDefaultResponse(Map<String, Object> response, Integer hits, Authentication authentication) {

		response.put("service", applicationName);
		response.put("port", port);
		response.put("hits", hits);
		if (authentication != null && !authentication.getAuthorities().isEmpty()) {
			List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
			response.put("roles", roles);
		}
		response.put("datetime", ZonedDateTime.now());
	}
}
