package com.kep.platform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;

@Service
@Slf4j
public class MockService {

	@Resource
	private WebClient externalOAuthWebClient;

	/**
	 * OAuth 테스트
	 */
//	@Scheduled(fixedRate = 5000)
	public void getAll() {

		externalOAuthWebClient.get()
				// 리소스 서버
				.uri("http://localhost:9002/api/v1/mock")
				.attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction
						.clientRegistrationId("codeclick"))
				.retrieve()
				.bodyToMono(String.class)
				.map(response -> "RESPONSE: " + response)
				.subscribe(log::info);
	}
}
