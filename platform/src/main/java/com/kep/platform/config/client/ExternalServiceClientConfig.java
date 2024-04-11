package com.kep.platform.config.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 외부 http 통신 클라이언트, 연동하는 플랫폼마다 필요할 경우 별도 생성
 */
@Configuration
@Profile({"!live"})
@Slf4j
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
								.addHandlerLast(new WriteTimeoutHandler(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)))
				.wiretap("reactor.netty.http.client.HttpClient",
						LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);

		return WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient));
	}

	@Bean
	public ReactiveClientRegistrationRepository reactiveClientRegistrationRepository(OAuth2ClientProperties oAuth2ClientProperties) {

		List<ClientRegistration> clientRegistrations = new ArrayList<>();

		oAuth2ClientProperties.getRegistration()
				.forEach((k, v) -> {
					String authorizationUri = oAuth2ClientProperties.getProvider().get(k).getAuthorizationUri();
					String tokenUri = oAuth2ClientProperties.getProvider().get(k).getTokenUri();
					ClientRegistration clientRegistration = ClientRegistration
							.withRegistrationId(k)
							.authorizationUri(authorizationUri)
							.tokenUri(tokenUri)
							.authorizationGrantType(new AuthorizationGrantType(v.getAuthorizationGrantType()))
							.clientId(v.getClientId())
							.clientSecret(v.getClientSecret())
							.redirectUri(v.getRedirectUri())
							.scope(v.getScope())
							.build();
					clientRegistrations.add(clientRegistration);
				});

		return new InMemoryReactiveClientRegistrationRepository(clientRegistrations);
	}

	@Bean
	public WebClient externalOAuthWebClient(ReactiveClientRegistrationRepository clientRegistrations) {
//						 ServerOAuth2AuthorizedClientRepository clientRepository) {

		HttpClient httpClient = HttpClient.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MILLIS)
				.responseTimeout(Duration.ofMillis(CONNECT_TIMEOUT_MILLIS))
				.doOnConnected(conn ->
						conn.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS))
								.addHandlerLast(new WriteTimeoutHandler(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)))
				.wiretap("reactor.netty.http.client.HttpClient",
						LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);

		AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authClientManager =
				new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations,
						new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations));

		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
				new ServerOAuth2AuthorizedClientExchangeFilterFunction(authClientManager);
//		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
//				new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, clientRepository);
		oauth.setDefaultOAuth2AuthorizedClient(true);
		oauth.setDefaultClientRegistrationId("kakao-bizmsg");

		return WebClient.builder()
//				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString())
//				.defaultHeader("Accept", MediaType.APPLICATION_JSON.toString())
				.filter(oauth)
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
	}

	@Bean
	public RestTemplate externalRestTemplate(RestTemplateBuilder builder) {

		return builder
				.setConnectTimeout(Duration.ofMillis(CONNECT_TIMEOUT_MILLIS))
				.setReadTimeout(Duration.ofMillis(READ_TIMEOUT_MILLIS))
				.build();
	}
}
