package com.kep.portal.config.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 외부 http 통신 클라이언트, 연동하는 플랫폼마다 필요할 경우 별도 생성
 */
@Configuration
public class ExternalServiceClientConfig {

	private static final int CONNECT_TIMEOUT_MILLIS = 20000;
	private static final int READ_TIMEOUT_MILLIS = 20000;

	@Bean
	WebClient.Builder externalWebClientBuilder() {

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
	public RestTemplate externalRestTemplate(RestTemplateBuilder builder) {

		return builder
				.setConnectTimeout(Duration.ofMillis(CONNECT_TIMEOUT_MILLIS))
				.setReadTimeout(Duration.ofMillis(READ_TIMEOUT_MILLIS))
				.build();
	}
}
