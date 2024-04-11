package com.kep.legacy.config.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * 외부 http 통신 클라이언트, 연동하는 플랫폼마다 필요할 경우 별도 생성
 */
@Configuration
public class ExternalServiceClientConfig {

	private static final int CONNECT_TIMEOUT_MILLIS = 20000;
	private static final int READ_TIMEOUT_MILLIS = 20000;

	@Bean
	public RestTemplate externalRestTemplate(RestTemplateBuilder builder) {

		return builder
				.setConnectTimeout(Duration.ofMillis(CONNECT_TIMEOUT_MILLIS))
				.setReadTimeout(Duration.ofMillis(READ_TIMEOUT_MILLIS))
				.build();
	}
}
