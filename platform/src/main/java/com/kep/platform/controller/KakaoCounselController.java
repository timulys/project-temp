package com.kep.platform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.event.PlatformEventDto;
import com.kep.core.model.dto.event.PlatformEventType;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.platform.model.dto.KakaoCounselReceiveEvent;
import com.kep.platform.service.SendToPortalProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * 카카오 상담톡 API
 *
 * 메세지 -> 파싱 -> 큐
 */
@Tag(name = "카카오 상담톡 API", description = "/api/v1/kakao-counsel-talk")
@RestController
@RequestMapping("/api/v1/kakao-counsel-talk")
@Slf4j
public class KakaoCounselController {

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private SendToPortalProducer producer;

	/**
	 * 메세지
	 */
	@Tag(name = "카카오 상담톡 API")
	@Operation(summary = "메시지 발송")
	@PostMapping(value = "/message", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResult<Map<String, Object>>> message(
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam Map<String, String> requestParams,
			@RequestBody String requestBody) throws Exception {

		log.info("KAKAO COUNSEL TALK, MESSAGE");

		for (String key : httpHeaders.keySet()) {
			log.info("KAKAO COUNSEL TALK, MESSAGE, HEADER: {}: {}", key, httpHeaders.get(key));
		}
		for (String key : requestParams.keySet()) {
			log.info("KAKAO COUNSEL TALK, MESSAGE, PARAM: {}: {}", key, requestParams.get(key));
		}
		log.info("KAKAO COUNSEL TALK, MESSAGE, BODY: {}", requestBody);

		PlatformEventDto platformEventDto = buildPlatformEvent(requestBody);
		platformEventDto.setPlatformEventType(PlatformEventType.MESSAGE);
		producer.sendMessage(platformEventDto);

		return new ResponseEntity<>(new ApiResult<>(ApiResultCode.succeed), HttpStatus.CREATED);
	}

	/**
	 * 상담 요청
	 */
	@Tag(name = "카카오 상담톡 API")
	@Operation(summary = "상담 요청")
	@PostMapping(value = "/reference", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResult<Map<String, Object>>> open(
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(required = false) Map<String, String> requestParams,
			@RequestBody String requestBody) throws Exception {

		log.info("KAKAO COUNSEL TALK, REFERENCE");

		for (String key : httpHeaders.keySet()) {
			log.info("KAKAO COUNSEL TALK, REFERENCE, HEADER: {}: {}", key, httpHeaders.get(key));
		}
		for (String key : requestParams.keySet()) {
			log.info("KAKAO COUNSEL TALK, REFERENCE, PARAM: {}: {}", key, requestParams.get(key));
		}
		log.info("KAKAO COUNSEL TALK, REFERENCE, BODY: {}", requestBody);

		PlatformEventDto platformEventDto = buildPlatformEvent(requestBody);
		platformEventDto.setPlatformEventType(PlatformEventType.OPEN);
		producer.sendMessage(platformEventDto);

		return new ResponseEntity<>(new ApiResult<>(ApiResultCode.succeed), HttpStatus.CREATED);
	}

	/**
	 * 상담 종료
	 */
	@Tag(name = "카카오 상담톡 API")
	@Operation(summary = "상담 종료")
	@PostMapping(value = "/expired_session")
	public ResponseEntity<ApiResult<Map<String, Object>>> close(
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(required = false) Map<String, String> requestParams,
			@RequestBody(required = false) String requestBody) throws Exception {

		log.info("KAKAO COUNSEL TALK, CLOSE");

		for (String key : httpHeaders.keySet()) {
			log.info("KAKAO COUNSEL TALK, CLOSE, HEADER: {}: {}", key, httpHeaders.get(key));
		}
		for (String key : requestParams.keySet()) {
			log.info("KAKAO COUNSEL TALK, CLOSE, PARAM: {}: {}", key, requestParams.get(key));
		}
		log.info("KAKAO COUNSEL TALK, CLOSE, BODY: {}", requestBody);

		PlatformEventDto platformEventDto = buildPlatformEvent(requestBody);
		platformEventDto.setPlatformEventType(PlatformEventType.CLOSE);
		producer.sendMessage(platformEventDto);

		return new ResponseEntity<>(new ApiResult<>(ApiResultCode.succeed), HttpStatus.CREATED);
	}

	@Tag(name = "카카오 상담톡 API")
	@Operation(summary = "핑")
	@RequestMapping
	public ResponseEntity<ApiResult<Map<String, Object>>> ping(
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(required = false) Map<String, String> requestParams,
			@RequestBody(required = false) String requestBody) {

		log.info("KAKAO COUNSEL TALK");

		for (String key : httpHeaders.keySet()) {
			log.info("KAKAO COUNSEL TALK, HEADER: {}: {}", key, httpHeaders.get(key));
		}
		for (String key : requestParams.keySet()) {
			log.info("KAKAO COUNSEL TALK, PARAM: {}: {}", key, requestParams.get(key));
		}
		log.info("KAKAO COUNSEL TALK, BODY: {}", requestBody);

		return new ResponseEntity<>(new ApiResult<>(ApiResultCode.succeed), HttpStatus.OK);
	}

	private PlatformEventDto buildPlatformEvent(@RequestBody String requestBody) throws JsonProcessingException {

		KakaoCounselReceiveEvent kakaoCounselReceiveEvent = objectMapper.readValue(requestBody, KakaoCounselReceiveEvent.class);
		return PlatformEventDto.builder()
				.platformType(PlatformType.kakao_counsel_talk)
				.serviceKey(kakaoCounselReceiveEvent.getSenderKey())
				.userKey(kakaoCounselReceiveEvent.getUserKey())
				.eventKey(String.valueOf(kakaoCounselReceiveEvent.getSerialNumber()))
				.payload(requestBody)
				.created(ZonedDateTime.now())
				.build();
	}
}
