package com.kep.platform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.platform.PlatformType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 솔루션 채널 API (customer web, legacy app ...)
 *
 * 메세지 -> 큐
 */
@RestController
@RequestMapping("/api/v1/{platform}")
@Slf4j
public class FormalPlatformController {

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private RabbitTemplate rabbitTemplate;

	@PostMapping(value = "/message")
	public ResponseEntity<ApiResult<Map<String, Object>>> message(
			@PathVariable(value = "platform") @NotEmpty String platform,
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam Map<String, String> requestParams,
			@RequestBody String requestBody) throws Exception {

		PlatformType platformType = getPlatformType(platform);
		Assert.notNull(platformType, "INVALID PLATFORM TYPE");
		log.info("PLATFORM: {}, RECEIVE, BODY: {}", platformType, requestBody);

		for (String key : httpHeaders.keySet()) {
			log.info("PLATFORM: {}, RECEIVE, HEADER: {}: {}", platformType, key, httpHeaders.get(key));
		}
		for (String key : requestParams.keySet()) {
			log.info("PLATFORM: {}, RECEIVE, PARAM: {}: {}", platformType, key, requestParams.get(key));
		}

		// TODO: global
		try {
			Map<String, Object> requestBodyMap = objectMapper.readValue(requestBody, new TypeReference<Map<String, Object>>() {});
			for (String key : requestBodyMap.keySet()) {
				log.info("PLATFORM: {}, POST ECHO, BODY: {}: {}", platformType, key, requestBodyMap.get(key));
			}

			Message message = MessageBuilder
					.withBody(requestBody.getBytes(StandardCharsets.UTF_8))
					.setContentType(MessageProperties.CONTENT_TYPE_JSON)
					.build();
			rabbitTemplate.send("kakao.topic", "kakao.first", message);

			requestBodyMap.putAll(requestParams);
			ApiResult<Map<String, Object>> apiResult = ApiResult.<Map<String, Object>>builder()
					.code(ApiResultCode.succeed)
					.payload(requestBodyMap).build();
			return new ResponseEntity<>(apiResult, null, HttpStatus.ACCEPTED);
		} catch (JsonProcessingException e) {
			log.error(e.getLocalizedMessage());
			log.error(e.getOriginalMessage());
			throw new IllegalArgumentException("BLAH BLAH");
		}
	}

	@GetMapping(value = "/open")
	public ResponseEntity<ApiResult<Map<String, Object>>> open(
			@PathVariable(value = "platform") @NotEmpty String platform,
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(required = false) Map<String, String> requestParams,
			Authentication authentication,
			HttpServletRequest request) {

		PlatformType platformType = getPlatformType(platform);
		Assert.notNull(platformType, "INVALID PLATFORM TYPE");
		log.info("PLATFORM: {}, OPEN", platformType);

		for (String key : httpHeaders.keySet()) {
			log.info("PLATFORM: {}, OPEN, HEADER: {}: {}", platformType, key, httpHeaders.get(key));
		}
		for (String key : requestParams.keySet()) {
			log.info("PLATFORM: {}, OPEN, PARAM: {}: {}", platformType, key, requestParams.get(key));
		}

		Map<String, Object> response = new HashMap<>(requestParams);
		if (authentication != null && !authentication.getAuthorities().isEmpty()) {
			List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
			response.put("roles", roles);
		}

		ApiResult<Map<String, Object>> apiResult = ApiResult.<Map<String, Object>>builder()
				.code(ApiResultCode.succeed)
				.payload(response).build();

		return new ResponseEntity<>(apiResult, HttpStatus.OK);
	}

	private PlatformType getPlatformType(@NotEmpty String platform) {
		try {
			// kebab-case to snake_case
			return PlatformType.valueOf(platform.replace("-", "_"));
		} catch (IllegalArgumentException e) {
			log.error("INVALID PLATFORM TYPE, {}", e.getLocalizedMessage());
			return null;
		}
	}
}
