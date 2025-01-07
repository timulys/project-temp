package com.dkt.always.talk.config;

import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

public class CustomAuthClient extends WebClientReactiveClientCredentialsTokenResponseClient {
    @Override
    public Mono<OAuth2AccessTokenResponse> getTokenResponse(OAuth2ClientCredentialsGrantRequest grantRequest) {
        WebClient.RequestBodySpec requestBodySpec = WebClient.create()
                .post()
                .uri(grantRequest.getClientRegistration().getProviderDetails().getTokenUri())
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        // Basic 인증 헤더 추가 (without Base64 encoding)
        String clientId = grantRequest.getClientRegistration().getClientId();
        String clientSecret = grantRequest.getClientRegistration().getClientSecret();
        requestBodySpec.header("Authorization", "Basic " + clientId + " " + clientSecret);

        // 인증 결과 mapping
        Mono<OAuth2AccessTokenResponse> tokenResponse = requestBodySpec
                .bodyValue(String.format("grant_type=%s", AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()))
                .retrieve()
                .bodyToMono(Map.class) // 응답을 Map으로 처리
                .map(response -> {
                    String accessToken = (String) response.get("access_token");
                    Long expiresIn = Long.valueOf((Integer) response.getOrDefault("expires_in", 3600));
                    return OAuth2AccessTokenResponse.withToken(accessToken)
                            .tokenType(OAuth2AccessToken.TokenType.BEARER)
                            .expiresIn(expiresIn)
                            .build();
                    }
                );

        return tokenResponse;
    }
}
