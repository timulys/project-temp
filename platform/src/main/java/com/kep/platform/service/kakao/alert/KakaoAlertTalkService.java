package com.kep.platform.service.kakao.alert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.*;
import com.kep.platform.config.property.PlatformProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 카카오 알림톡 API
 */
@Service
@Slf4j
public class KakaoAlertTalkService {

//    private static final String ALERT_TALK_SEND_PATH = "/v1/message/multi/send"; // POST 알림톡 발송
    private static final String ALERT_TALK_RESULT_PATH = "/v1/info/message/results"; // GET 알림톡 결과 요청
    private static final String ALERT_TALK_COMPLETE_PATH = "/v1/info/message/results/complete"; //  {reportGroupNo} PUT 결과 처리 완료
    private static final String ALERT_TALK_SEARCH_PATH = "/v1/info/message/search"; // POST 발송 내역 조회
    private static final String ALERT_TALK_DETAIL_PATH = "/v1/info/message/search/detail"; // {uid} GET 발송 내역 상세 조회

    /**
     * V3 이상 BZM API
     */
    private static final String ALERT_TALK_SEND_PATH = "/alimtalk/send"; // (V3)알림톡 발송

    @Resource
    private PlatformProperty platformProperty;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private WebClient externalOAuthWebClient;
    @Resource
    private WebClient kakaoTemplateWebClient;

    /**
     * 알림톡 발송
     */
    public KakaoBizTalkSendResponse send(@Valid KakaoAlertSendEvent dto, @Positive Long trackKey) {

        log.info("KAKAO ALERT TALK, SEND, TRACK KEY: {}, DTO: {}", trackKey, dto);
        String bodyJson;
        try {
            bodyJson = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        KakaoBizTalkSendResponse talkResponse = kakaoTemplateWebClient.post()
                .uri(getRequestUrl(ALERT_TALK_SEND_PATH, "v3"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(bodyJson))
                .retrieve()
                .bodyToMono(KakaoBizTalkSendResponse.class)
                .block();
        log.info("talkResponse = {}", talkResponse);

        return talkResponse;
    }

    /**
     * 결과 요청
     */
    public KakaoBizTalkSendResponse result(@Positive Long trackKey) {
        log.info("KAKAO ALERT TALK, RESULT, TRACK KEY: {}", trackKey);

        KakaoBizTalkSendResponse sendResponse = externalOAuthWebClient.get().uri(getRequestUrl(ALERT_TALK_RESULT_PATH, "v3")).retrieve().bodyToMono(KakaoBizTalkSendResponse.class).block();

        log.info("sendResponse = {}", sendResponse);

        return sendResponse;

    }

    /**
     * 결과 처리 완료
     */
    public KakaoBizTalkSendResponse complete(@Positive Long trackKey, String reportGroupNo) {

        log.info("KAKAO ALERT TALK, COMPLETE, TRACK KEY: {}", trackKey);

        KakaoBizTalkSendResponse sendResponse = externalOAuthWebClient.put().uri(getRequestUrl(ALERT_TALK_COMPLETE_PATH, "v3") + "/" + reportGroupNo).retrieve().bodyToMono(KakaoBizTalkSendResponse.class).block();

        log.info("sendResponse = {}", sendResponse);

        return sendResponse;


    }

    /**
     * 발송 내역 조회
     */
    public KakaoBizSearchResponse search(@Positive Long trackKey, KakaoBizSearchSendEvent dto) {

        log.info("KAKAO ALERT TALK, SEARCH, TRACK KEY: {}", trackKey);

        KakaoBizSearchResponse searchResponse = externalOAuthWebClient.post().uri(getRequestUrl(ALERT_TALK_SEARCH_PATH, "v3")).bodyValue(dto).retrieve().bodyToMono(KakaoBizSearchResponse.class).block();

        log.info("searchResponse = {}", searchResponse);

        return searchResponse;


    }

    /**
     * 발송 내역 상세 조회
     */
    public KakaoBizDetailResponse searchDetail(@Positive Long trackKey, String uid) {

        log.info("KAKAO ALERT TALK, SEARCH DETAIL, TRACK KEY: {}", trackKey);

        KakaoBizDetailResponse detailResponse = externalOAuthWebClient.get().uri(getRequestUrl(ALERT_TALK_DETAIL_PATH, "v3") + "/" + uid).retrieve().bodyToMono(KakaoBizDetailResponse.class).block();

        log.info("detailResponse = {}", detailResponse);
        return detailResponse;

    }

    private String getRequestUrl(@NotNull String endPoint, @NotNull String version) {

        PlatformProperty.Platform platform = platformProperty.getPlatforms().get(PlatformType.kakao_alert_talk.name());
        Assert.notNull(platform, "PLATFORM IS NULL");

        return String.format("%s/%s/%s%s", platform.getApiBaseUrl(), version, platform.getApiKey(), endPoint);
    }
}
