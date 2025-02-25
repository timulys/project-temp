package com.kep.platform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.platform.config.property.KaKaoSyncProperties;
import com.kep.platform.service.kakao.sync.KakaoSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 플랫폼 인증
 */
@Tag(name = "플랫폼 인증 API", description = "/auth")
@Controller
@RequestMapping("/auth")
@Slf4j
public class PlatformAuthController {


	@Value("${server.port}")
	private int port;
	@Value("${spring.application.name}")
	private String applicationName;
	@Value("${spring.security.oauth2.client.provider.kakao-sync.authorization-uri}")
    private String kakaoAuthorizationUri;
	@Resource
	private KaKaoSyncProperties kaKaoSyncProperties;
	
	@Resource
	private KakaoSyncService kakaoSyncService;

	@Tag(name = "플랫폼 인증 API")
	@Operation(summary = "홈(카카오 싱크)")
	@GetMapping(value = "/kakao-sync/code")
	public String get(
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(required = false) Map<String, String> requestParams) {

		log.info("AUTH, KAKAO SYNC, CODE, HEADER: {}, PARAMS: {}", httpHeaders, requestParams);

		return "home";
	}

	@Tag(name = "플랫폼 인증 API")
	@Operation(summary = "카카오 인증 후 리다이렉트 URL 조회")
	@GetMapping(value = "/kakao-sync/authorized")
	@ResponseBody
	public ResponseEntity<?> get(
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(required = false) Map<String, String> requestParams,
			HttpSession httpSession,
			Authentication authentication) throws Exception {

		log.info("AUTH, KAKAO SYNC, AUTHORIZED, PARAMS: {}", requestParams);

		String code = requestParams.get("code");
		String syncRedirectUrl = "";
		if (!ObjectUtils.isEmpty(code)) {
			syncRedirectUrl = kakaoSyncService.authorized(code, requestParams);
		}

		if(!ObjectUtils.isEmpty(syncRedirectUrl)) {
			HttpHeaders headers = new HttpHeaders();
	        headers.setLocation(new URI(syncRedirectUrl));
	        log.info("▶▶▶::카카오 인증 후 리다이렉트 URL: {} ",syncRedirectUrl);

			return ResponseEntity.status(HttpStatus.OK).body(syncRedirectUrl);
		}
		
		Map<String, Object> response = new HashMap<>(requestParams);

		log.info("KAKAO SYNC , RESPONSE:{}", response);
		setDefaultResponse(response, null, authentication);
		ApiResult<Map<String, Object>> apiResult = ApiResult.<Map<String, Object>>builder()
				.code(ApiResultCode.succeed)
				.build();

		return new ResponseEntity<>(apiResult, HttpStatus.OK);
	}

	@Tag(name = "플랫폼 인증 API")
	@Operation(summary = "카카오 OAuth 인증 페이지 리다이렉트")
	@GetMapping(value = "/kakao-sync/getSync")
	public ResponseEntity<String> customSyncRequest(@RequestHeader HttpHeaders httpHeaders, @RequestParam String state){
		log.info("▶▶▶::카카오 커스텀 싱크 요청 URL: {}", state);
		String jsonState = convertStateToJson(state);

		// URL 인코딩을 추가
	    try {
	        jsonState = URLEncoder.encode(jsonState, StandardCharsets.UTF_8.toString());
	    } catch (UnsupportedEncodingException e) {
	        log.error("URL 인코딩 중 오류 발생", e);
	        throw new RuntimeException("URL 인코딩 실패", e);
	    }

	    // 카카오 OAuth 인증 페이지로 리다이렉트할 URL을 생성
	    String oauthUrl = kakaoAuthorizationUri + "?client_id=" + kaKaoSyncProperties.getClientId() +
	            "&response_type=code&redirect_uri=" + kaKaoSyncProperties.getRedirectUri() +
	            "&state=" + jsonState;

		return ResponseEntity.status(HttpStatus.OK).body(oauthUrl);
	}
	
	private String convertStateToJson(String state) {
	    Map<String, String> stateMap = new HashMap<>();

		// TODO : State 값이 많아지면 어떻게 Split 기준을 삼을 것인지 파악 필요
		// FIXME : _로 구분된 key, value에 대한 구조적 재설계 필요
		Arrays.stream(state.split("__"))
				.map(pair -> pair.split("_")) // Key : Value
				.forEach(arr -> stateMap.put(arr[0], arr[1]));

	    try {
	        // Map을 JSON 문자열로 변환
	        String jsonResult = new ObjectMapper().writeValueAsString(stateMap);
	        log.info("Converted JSON: {}", jsonResult);

	        return jsonResult;
	    } catch (JsonProcessingException e) {
	        log.error("Error converting state to JSON", e);
	        throw new RuntimeException("Error converting state to JSON", e);
	    }
	}

	@Tag(name = "플랫폼 인증 API")
	@Operation(summary = "로그인")
	@PostMapping(value = "/login")
	public ResponseEntity<ApiResult<Map<String, Object>>> post(
			@RequestParam(required = false) Map<String, String> requestParams,
			@RequestBody(required = false) Map<String, Object> requestBody) {

		log.info("AUTH, LOGIN, POST");

		Map<String, Object> response = new HashMap<>(requestParams);
		ApiResult<Map<String, Object>> apiResult = ApiResult.<Map<String, Object>>builder()
				.code(ApiResultCode.succeed)
				.payload(response).build();

		return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
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
