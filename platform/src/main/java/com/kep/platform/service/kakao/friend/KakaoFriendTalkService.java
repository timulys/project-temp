package com.kep.platform.service.kakao.friend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.*;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import com.kep.platform.config.property.PlatformProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.Optional;

/**
 * 카카오 친구톡 API
 */
@Service
@Slf4j
public class KakaoFriendTalkService {
    public static final String FRIEND_TALK_SEND_PATH = "/v1/ft/multi/send"; // POST 발송
    public static final String FRIEND_TALK_UPLOAD_IMAGE_PATH = "/v1/upload/ft/image"; // POST 이미지 업로드
    public static final String FRIEND_TALK_UPLOAD_WIDE_IMAGE_PATH = "/v1/upload/ft/wide/image"; // POST 와이드 이미지 업로드
    public static final String FRIEND_TALK_SEARCH_PATH = "/v1/info/ft/search"; // GET 발송 내역 조회
    public static final String FRIEND_TALK_DETAIL_PATH = "/v1/info/ft/search/detail"; // {uid} GET 발송 상세 내역 조회

    @Resource
    private PlatformProperty platformProperty;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private WebClient externalOAuthWebClient;

    /**
     * 친구톡 발송
     */
    public KakaoBizTalkSendResponse send(KakaoFriendSendEvent dto, @Positive Long trackKey) {

        log.info("KAKAO FRIEND TALK, SEND, TRACK KEY: {}", trackKey);
        String bodyJson;
        try {
            bodyJson = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        KakaoBizTalkSendResponse sendResponse = externalOAuthWebClient.post().uri(getRequestUrl(FRIEND_TALK_SEND_PATH))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(bodyJson)).retrieve().bodyToMono(KakaoBizTalkSendResponse.class).block();

        log.info("sendResponse = {}", sendResponse);
        return sendResponse;
    }


    /**
     * 이미지 업로드
     */
    public KakaoBizTalkSendResponse uploadImage(@Positive Long trackKey, UploadPlatformRequestDto dto) {
        if ("wide".equals(dto.getImageType())) {
            return uploadImage(trackKey, true, dto.getSourceFile());
        } else {
            return uploadImage(trackKey, false, dto.getSourceFile());
        }
    }

    /**
     * 이미지 업로드
     * <li>권장 사이즈 : 720px*720px
     * <li>제한 사이즈 - 가로 500px 미만 또는 가로:세로 비율이 2:1 미만 또는 3:4 초과시 업로드 불가
     * <li>파일형식 및 크기 : jpg, png / 최대 500KB
     *
     * <li>제한 사이즈 : 800px*600px
     * <li>파일형식 및 크기 : jpg, png / 최대 2MB
     */
    public KakaoBizTalkSendResponse uploadImage(@Positive Long trackKey, boolean isWide, MultipartFile mFile) {

        log.info("KAKAO FRIEND TALK, UPLOAD IMAGE, WIDE: {}, TRACK KEY: {}", isWide, trackKey);
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("image", mFile.getResource());

        KakaoBizTalkSendResponse templateResponse = null;
        if (isWide) {
            templateResponse = externalOAuthWebClient.post().uri(getRequestUrl(FRIEND_TALK_UPLOAD_WIDE_IMAGE_PATH)).body(BodyInserters.fromMultipartData(parts)).retrieve().bodyToMono(KakaoBizTalkSendResponse.class).block();
        } else {
            templateResponse = externalOAuthWebClient.post().uri(getRequestUrl(FRIEND_TALK_UPLOAD_IMAGE_PATH)).body(BodyInserters.fromMultipartData(parts)).retrieve().bodyToMono(KakaoBizTalkSendResponse.class).block();
        }
        log.info("templateResponse = {}", templateResponse);
        return templateResponse;

    }

    /**
     * 발송 내역 조회
     */
    public KakaoBizSearchResponse search(@Positive Long trackKey, KakaoBizSearchSendEvent dto) {

        log.info("KAKAO FRIEND TALK, RESULT, TRACK KEY: {}, SEARCH CONDITION: {}", trackKey, dto);

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(FRIEND_TALK_SEARCH_PATH)
                .queryParamIfPresent("start_date", Optional.ofNullable(dto.getStartDate()))
                .queryParamIfPresent("end_date", Optional.ofNullable(dto.getEndDate()))
                .queryParamIfPresent("status_code", Optional.ofNullable(dto.getStatusCode()))
                .queryParamIfPresent("etc1", Optional.ofNullable(dto.getEtc1()))
                .queryParamIfPresent("etc2", Optional.ofNullable(dto.getEtc2()))
                .queryParamIfPresent("size", Optional.ofNullable(dto.getSize()))
                .queryParamIfPresent("page", Optional.ofNullable(dto.getPage()))
                .queryParamIfPresent("last_uid", Optional.ofNullable(dto.getLastUid()));

        builder.scheme(getBaseUrl().split("://")[0]);
        builder.host(getBaseUrl().split("://")[1]);
        URI uri = builder.build().toUri();

        KakaoBizSearchResponse searchResponse = externalOAuthWebClient.get().uri(uri)
                .retrieve().bodyToMono(KakaoBizSearchResponse.class).block();

        log.info("searchResponse = {}", searchResponse);

        return searchResponse;
    }

    public KakaoBizDetailResponse detail(Long trackKey, String uid) {
        log.info("KAKAO FRIEND TALK, SEARCH DETAIL, TRACK KEY: {}", trackKey);

        KakaoBizDetailResponse detailResponse = externalOAuthWebClient.get().uri(getRequestUrl(FRIEND_TALK_DETAIL_PATH) + "/" + uid).retrieve().bodyToMono(KakaoBizDetailResponse.class).block();

        log.info("detailResponse = {}", detailResponse);
        return detailResponse;
    }

    private String getRequestUrl(@NotNull String endPoint) {
        PlatformProperty.Platform platform = platformProperty.getPlatforms().get(PlatformType.kakao_friend_talk.name());
        Assert.notNull(platform, "PLATFORM IS NULL");

        return platform.getApiBaseUrl() + endPoint;
    }

    private String getBaseUrl() {
        PlatformProperty.Platform platform = platformProperty.getPlatforms().get(PlatformType.kakao_friend_talk.name());
        Assert.notNull(platform, "PLATFORM IS NULL");

        return platform.getApiBaseUrl();
    }
}
