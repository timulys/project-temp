package com.dkt.always.talk.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebClientConfig {
    private static final int CONNECT_TIMEOUT_MILLIS = 20000;
    private static final int READ_TIMEOUT_MILLIS = 20000;

    @Bean
    public ReactiveClientRegistrationRepository reactiveClientRegistrationRepository(OAuth2ClientProperties oAuth2ClientProperties) {
        List<ClientRegistration> clientRegistrations = new ArrayList<>();

        oAuth2ClientProperties.getRegistration().forEach((k, v) -> {
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

    /**
     * Kakao Biz Talk Service Call WebClient
     * @return
     */
    @Bean
    public WebClient kakaoBizTalkWebClient(ReactiveClientRegistrationRepository clientRegistrations) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MILLIS)
                .responseTimeout(Duration.ofMillis(CONNECT_TIMEOUT_MILLIS))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)))
                .wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);

        CustomAuthClient customAuthClient = new CustomAuthClient();
        ReactiveOAuth2AuthorizedClientProvider provider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials(c -> c.accessTokenResponseClient(customAuthClient))
                .build();

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations,
                        new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations));
        manager.setAuthorizedClientProvider(provider);

        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(manager);
        oauth.setDefaultOAuth2AuthorizedClient(true);
        oauth.setDefaultClientRegistrationId("kakao-bizmsg");

        return WebClient.builder()
                .filter(oauth)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
