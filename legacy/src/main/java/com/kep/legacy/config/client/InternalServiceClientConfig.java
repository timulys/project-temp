package com.kep.legacy.config.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * 내부 http 통신 클라이언트 (로드 밸런서)
 */
@Configuration
@EnableRetry
public class InternalServiceClientConfig {

	private static final int CONNECT_TIMEOUT_MILLIS = 20000;
	private static final int READ_TIMEOUT_MILLIS = 20000;

	@Bean
	@Primary
	@LoadBalanced
	public RestTemplate restTemplate(RestTemplateBuilder builder) {

		return builder
				.setConnectTimeout(Duration.ofMillis(CONNECT_TIMEOUT_MILLIS))
				.setReadTimeout(Duration.ofMillis(READ_TIMEOUT_MILLIS))
				.build();
	}
}
