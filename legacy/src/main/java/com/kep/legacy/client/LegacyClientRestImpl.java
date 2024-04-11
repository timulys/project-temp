package com.kep.legacy.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.legacy.config.property.LegacyProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Map;

/**
 * 기간계 호출 (REST)
 */
@Component
@Slf4j
public class LegacyClientRestImpl implements LegacyClient {

	@Resource
	private RestTemplate externalRestTemplate;
	@Resource
	private LegacyProperty legacyProperty;
	@Resource
	private ObjectMapper objectMapper;

	@Override
	@Retryable(value = {RestClientException.class},
			maxAttempts = 3,
			backoff = @Backoff(delay = 1000, multiplier = 2))
	public Map<String, Object> request(@NotNull String path,
			@NotNull Map<String, Object> requestBody,
			@Positive Long trackKey) throws Exception {

//		requestBody.put("IF_KEY", "1234");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

		log.info("LEGACY CLIENT, TRACK KEY: {}, BODY: {}", trackKey, requestBody);
		String requestUrl = legacyProperty.getApiBaseUrl() + path;

		ResponseEntity<String> responseEntity = externalRestTemplate.exchange(
				requestUrl, HttpMethod.GET, request, String.class);
		log.info("LEGACY CLIENT, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
				trackKey, responseEntity.getStatusCode(), responseEntity.getBody());

		String responseBody = responseEntity.getBody();
		Map<String, Object> response = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});

		return response;
	}
}
