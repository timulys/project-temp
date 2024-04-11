package com.kep.platform.config.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

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
	WebClient.Builder webClientBuilder() {

		HttpClient httpClient = HttpClient.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MILLIS)
				.responseTimeout(Duration.ofMillis(CONNECT_TIMEOUT_MILLIS))
				.doOnConnected(conn ->
						conn.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS))
								.addHandlerLast(new WriteTimeoutHandler(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)));

		return WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient));
	}

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
