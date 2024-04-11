package com.kep.legacy.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.legacy.config.property.LegacyProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Collections;
import java.util.Map;

/**
 * 기간계 호출 (고객사 제공 라이브러리 사용)
 */
//@Component
@Slf4j
public class LegacyClientMockImpl implements LegacyClient {

	@Resource
	private LegacyProperty legacyProperty;
	@Resource
	private ObjectMapper objectMapper;

	@Override
	@Retryable(value = {RestClientException.class}
			, maxAttempts = 3
			, backoff = @Backoff(delay = 1000, multiplier = 2))
	public Map<String, Object> request(@NotNull String path,
			@NotNull Map<String, Object> requestBody,
			@Positive Long trackKey) throws Exception {

		log.info("LEGACY CLIENT, TRACK KEY: {}, BODY: {}", trackKey, requestBody);

//		legacySupportedClient.send();

		return Collections.emptyMap();
	}
}
