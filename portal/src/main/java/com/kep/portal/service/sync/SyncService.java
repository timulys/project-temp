package com.kep.portal.service.sync;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.model.dto.sync.SyncInfo;
import com.kep.portal.model.entity.customer.CustomerContact;
import com.kep.portal.repository.customer.CustomerContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * platform 모듈에서 처리
 */
@Service
@Deprecated
@Transactional
@Slf4j
public class SyncService {

    @Resource
    private WebClient.Builder externalWebClientBuilder;

    @Resource
    private CustomerContactRepository customerContactRepository;

    @Resource
    private PortalProperty portalProperty;

    @Resource
    private ObjectMapper objectMapper;

    private String CLIENT_ID = "ed63e1ac2f8cdbd67a3e041f2ef32c8e";

    public String getAccessToken(String authorize_code) {
        WebClient webClient = externalWebClientBuilder
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String redirectUri = portalProperty.getServiceUrl()+portalProperty.getApiBasePath()+"/sync/callback";

        Map response = webClient.post().uri(uriBuilder -> uriBuilder
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", CLIENT_ID)
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("code", authorize_code)
                        .build())
                .retrieve().bodyToMono(Map.class).block();

        String accessToken = response.get("access_token").toString();
        log.info("access_token = {}", response);

        return accessToken;
    }
    //해당 부분 카카오싱크로 콜백url db에 어떻게 저장되는지 확인
    public SyncInfo getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        WebClient webClient = externalWebClientBuilder
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String response = webClient.post().uri(uriBuilder -> uriBuilder
                        .path("/v2/user/me").build())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve().bodyToMono(String.class).block();

        SyncInfo userInfo = objectMapper.readValue(response, SyncInfo.class);

        log.info("userInfo = {}", objectMapper.writeValueAsString(userInfo));

        return userInfo;
    }


}
