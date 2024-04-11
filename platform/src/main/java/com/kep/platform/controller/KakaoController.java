package com.kep.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 카카오 API (현재 미사용)
 */
@RestController
@RequestMapping("/api/v1/kakao")
@Slf4j
public class KakaoController {

	@Resource
	private ObjectMapper objectMapper;

	@PostMapping(value = "/oauth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResult<Map<String, Object>>> oauth(
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam Map<String, String> requestParams,
			@RequestBody(required = false) String requestBody) throws Exception {

		log.info("KAKAO, OAUTH");

		for (String key : httpHeaders.keySet()) {
			log.info("KAKAO, OAUTH, HEADER: {}: {}", key, httpHeaders.get(key));
		}
		for (String key : requestParams.keySet()) {
			log.info("KAKAO, OAUTH, PARAM: {}: {}", key, requestParams.get(key));
		}
		log.info("KAKAO, OAUTH, BODY: {}", requestBody);

		return new ResponseEntity<>(new ApiResult<>(ApiResultCode.succeed), HttpStatus.OK);
	}
}
