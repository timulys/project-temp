package com.kep.platform.service.kakao.alert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.*;
import com.kep.core.model.dto.platform.kakao.profile.KakaoSendProfileResponse;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import com.kep.platform.config.property.PlatformProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * 카카오 비즈톡, 템플릿 API
 */
@Service
@Slf4j
public class KakaoBizTalkTemplateService {

    /**
     * TEMPLATE API
     */
    public static final String TEMPLATE_MODIFY_PATH = "/mng/v1/resell/template/modify"; // {sendProfileKey} PUT 템플릿 수정
    public static final String TEMPLATE_DORMANT_PATH = "/mng/v1/resell/template/dormant/release"; // {sendProfileKey}/{templateCode} 템플릿 휴면 해제
    public static final String TEMPLATE_CANCEL_REQUEST_PATH = "/mng/v1/resell/template/cancel/request"; // {sendProfileKey}/{templateCode} 템플릿 검수요청 취소
    public static final String TEMPLATE_CANCEL_APPROVAL_PATH = "/mng/v1/resell/template/cancel/approval"; // {sendProfileKey}/{templateCode} 템플릿 승인 취소
    public static final String TEMPLATE_CREATE_PATH = "/mng/v1/resell/template/create"; // {sendProfileKey} POST 템플릿 등록
    public static final String TEMPLATE_SELECT_PATH = "/mng/v1/resell/template/select"; // {sendProfileKey} or {sendProfileKey}/{templateCode} GET 템플릿 검색
    public static final String TEMPLATE_LAST_MODIFY_PATH = "/mng/v1/resell/template/lastModified/select"; // {sendProfileKey} GET 변경된 템플릿 검색
    public static final String TEMPLATE_CATEGORY_ALL_PATH = "/mng/v1/resell/template/category/all"; // GET 카테고리 전체 조회
    public static final String TEMPLATE_CATEGORY_CODE_PATH = "/mng/v1/resell/template/category"; // {categoryCode} GET 카테고리 조회
    public static final String TEMPLATE_REMOVE_PATH = "/mng/v1/resell/template/remove"; // {sendProfileKey}/{templateCode} DELETE 템플릿 삭제
    public static final String TEMPLATE_UPLOAD_IMAGE_PATH = "/mng/v1/upload/image/alimtalk/template"; // POST

    public static final String TEMPLATE_HIGHLIGHT_UPLOAD_IMAGE_PATH = "/mng/v1/upload/image/alimtalk/itemHighlight"; // POST 하이라이트 이미지 업로드


    /**
     * 발신프로필 관리 API
     */
//    public static final String TEMPLATE_PROFILE_RECOVER_PATH = "/mng/v1/sendProfile/recover"; // {sendProfileKey} PUT 발신프로필 휴면해제
//    public static final String TEMPLATE_PROFILE_CREATE_PATH = "/mng/v1/sendProfile/create"; // 발신프로필 등록 POST
//    public static final String TEMPLATE_PROFILE_TOKEN_PATH = "/mng/v1/sendProfile/token"; // 발신프로필 토큰 조회 GET
    public static final String TEMPLATE_PROFILE_SELECT_PATH = "/mng/v1/sendProfile/select"; // 발신프로필 리스트 조회 GET
    public static final String TEMPLATE_PROFILE_CATEGORY_ALL_PATH = "/mng/v1/sendProfile/category/all"; // 발신프로필 카테고리 전체 조회 GET
    public static final String TEMPLATE_PROFILE_CATEGORY_CODE_PATH = "/mng/v1/sendProfile/category"; // {categoryCode} GET 발신프로필 카테고리 조회

    @Resource
    private PlatformProperty platformProperty;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private WebClient externalOAuthWebClient;

    /**
     * 카테고리 조회
     */
    public KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.TemplateCategory>> category(@Positive Long trackKey) {

        log.info("KAKAO TEMPLATE, CATEGORY, TRACK KEY: {}", trackKey);
        KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.TemplateCategory>> templateResponse = externalOAuthWebClient.get().uri(getRequestUrl(TEMPLATE_CATEGORY_ALL_PATH)).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template")).retrieve().bodyToMono(new ParameterizedTypeReference<KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.TemplateCategory>>>() {
        }).block();
        log.info("templateResponse = {}", templateResponse);
        return templateResponse;
    }

    public KakaoBizTemplateResponse<KakaoBizTemplateResponse.TemplateCategory> getCategoryCode(Long trackKey, String categoryCode) {
        log.info("KAKAO TEMPLATE, CATEGORY_CODE, TRACK KEY: {}, CODE: {}", trackKey, categoryCode);
        KakaoBizTemplateResponse<KakaoBizTemplateResponse.TemplateCategory> templateResponse = externalOAuthWebClient.get().uri(getRequestUrl(TEMPLATE_CATEGORY_CODE_PATH) + "/" + categoryCode).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template")).retrieve().bodyToMono(new ParameterizedTypeReference<KakaoBizTemplateResponse<KakaoBizTemplateResponse.TemplateCategory>>() {
        }).block();
        log.info("templateResponse = {}", templateResponse);
        return templateResponse;
    }

    /**
     * 이미지 업로드
     * <li>제한 사이즈 - 가로 500px 이상, 가로:세로 비율 2:1
     * <li>파일형식 및 크기 : jpg, png / 최대 500KB
     */
    public KakaoBizTemplateResponse uploadImage(@Positive Long trackKey, UploadPlatformRequestDto dto) {

        log.info("KAKAO TEMPLATE, UPLOAD IMAGE, TRACK KEY: {}", trackKey);
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("image", dto.getSourceFile().getResource());
        KakaoBizTemplateResponse templateResponse = externalOAuthWebClient.post().uri(getRequestUrl(TEMPLATE_UPLOAD_IMAGE_PATH)).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template")).body(BodyInserters.fromMultipartData(parts)).retrieve().bodyToMono(KakaoBizTemplateResponse.class).block();
        log.info("templateResponse = {}", templateResponse);
        return templateResponse;
    }

    /**
     * 이미지 업로드
     * <li> 제한 사이즈 : 가로 108px 이상, 가로:세로 비율 1:1
     * <li> 파일형식 및 크기 : jpg, png / 최대 500KB
     */
    public KakaoBizTemplateResponse highlightUploadImage(@Positive Long trackKey, UploadPlatformRequestDto dto) {

        log.info("KAKAO TEMPLATE, HIGHLIGHT UPLOAD IMAGE, TRACK KEY: {}", trackKey);
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("image", dto.getSourceFile().getResource());
        KakaoBizTemplateResponse templateResponse = externalOAuthWebClient.post().uri(getRequestUrl(TEMPLATE_HIGHLIGHT_UPLOAD_IMAGE_PATH)).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template")).body(BodyInserters.fromMultipartData(parts)).retrieve().bodyToMono(KakaoBizTemplateResponse.class).block();
        log.info("templateResponse = {}", templateResponse);
        return templateResponse;
    }

    /**
     * 템플릿 등록
     */
    public KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> create(@Positive Long trackKey, KakaoBizMessageTemplatePayload dto, String profileKey) {

        log.info("KAKAO TEMPLATE, CREATE, TRACK KEY: {}", trackKey);
        KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> templateResponse = externalOAuthWebClient.post().uri(getRequestUrl(TEMPLATE_CREATE_PATH) + "/" + profileKey).bodyValue(dto).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template")).retrieve().bodyToMono(new ParameterizedTypeReference<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>() {
        }).block();

        log.info("templateResponse = {}", templateResponse);
        return templateResponse;
    }

    /**
     * 템플릿 수정
     */
    public KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> modify(
            @Positive Long trackKey, String profileKey,
            KakaoBizMessageTemplatePayload dto
    ) {
        log.info("KAKAO TEMPLATE, MODIFY, TRACK KEY: {}, DTO: {}", trackKey, dto);

        Assert.notNull(dto.getTemplateCode(),"TemplateCode must not be null");
        Assert.notNull(dto.getNewCategoryCode(),"NewTemplateCategoryCode must not be null");
        Assert.notNull(dto.getNewTemplateCode(),"NewTemplateCode must not be null");

        KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> templateResponse = externalOAuthWebClient.put().uri(getRequestUrl(TEMPLATE_MODIFY_PATH) + "/" + profileKey).bodyValue(dto).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template")).retrieve().bodyToMono(new ParameterizedTypeReference<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>() {
        }).block();
        log.info("templateResponse = {}", templateResponse);

        return templateResponse;

    }

    /**
     * 템플릿 삭제
     */
    public KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> remove(@Positive Long trackKey, String profileKey, @NotEmpty String templateCode) {

        log.info("KAKAO TEMPLATE, REMOVE, TRACK KEY: {}", trackKey);

        KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> templateResponse = externalOAuthWebClient.delete().uri(getRequestUrl(TEMPLATE_REMOVE_PATH) + "/" + profileKey + "/" + templateCode).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template")).retrieve().bodyToMono(new ParameterizedTypeReference<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>() {
        }).block();

        log.info("templateResponse = {}", templateResponse);
        return templateResponse;

    }

    /**
     * 템플릿 조회
     */
    public KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> select(@Positive Long trackKey, String profileKey, @NotEmpty String templateCode) {

        log.info("KAKAO TEMPLATE, SELECT, TRACK KEY: {}", trackKey);

        KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> templateResponse = externalOAuthWebClient.get().uri(getRequestUrl(TEMPLATE_SELECT_PATH) + "/" + profileKey + "/" + templateCode).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template")).retrieve().bodyToMono(new ParameterizedTypeReference<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>() {
        }).block();
        log.info("templateResponse = {}", templateResponse);

        return templateResponse;


    }

    /**
     * 변경된 템플릿 조회
     */
    public KakaoBizTemplateResponse<List<KakaoBizMessageTemplatePayload>> lastModified(@Positive Long trackKey, String profileKey) {

        log.info("KAKAO TEMPLATE, LAST MODIFIED, TRACK KEY: {}", trackKey);

        KakaoBizTemplateResponse<List<KakaoBizMessageTemplatePayload>> templateResponse = externalOAuthWebClient.get().uri(getRequestUrl(TEMPLATE_LAST_MODIFY_PATH) + "/" + profileKey).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template")).retrieve().bodyToMono(new ParameterizedTypeReference<KakaoBizTemplateResponse<List<KakaoBizMessageTemplatePayload>>>() {
        }).block();
        log.info("templateResponse = {}", templateResponse);

        return templateResponse;
    }


    public KakaoBizTemplateResponse<List<KakaoBizMessageTemplatePayload>> selectAll(Long trackKey, String profileKey, KakaoBizSearchSendEvent dto) {
        log.info("KAKAO TEMPLATE, SELECT, TRACK KEY: {}", trackKey);

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(TEMPLATE_SELECT_PATH + "/" + profileKey)
                .queryParamIfPresent("startDate", Optional.ofNullable(dto.getStartDate()))
                .queryParamIfPresent("endDate", Optional.ofNullable(dto.getEndDate()))
                .queryParamIfPresent("kepStatus", Optional.ofNullable(dto.getKepStatus()))
                .queryParamIfPresent("page", Optional.ofNullable(dto.getPage()))
                .queryParamIfPresent("rows", Optional.ofNullable(dto.getRows()));

        builder.scheme(getBaseUrl().split("://")[0]);
        builder.host(getBaseUrl().split("://")[1]);
        URI uri = builder.build().toUri();

        KakaoBizTemplateResponse<List<KakaoBizMessageTemplatePayload>> templateResponse = externalOAuthWebClient.get().uri(uri).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template"))
                .retrieve().bodyToMono(new ParameterizedTypeReference<KakaoBizTemplateResponse<List<KakaoBizMessageTemplatePayload>>>() {
                }).block();
        log.info("templateResponse = {}", templateResponse);

        return templateResponse;
    }

    public KakaoBizTemplateResponse cancelRequest(Long trackKey, String profileKey, String templateCode) {
        log.info("KAKAO TEMPLATE, CANCEL REQUEST, TRACK KEY: {}, PROFILE KEY: {}, TEMPLATE CODE: {}", trackKey, profileKey, templateCode);
        KakaoBizTemplateResponse templateResponse = externalOAuthWebClient.put().uri(getRequestUrl(TEMPLATE_CANCEL_REQUEST_PATH) + "/" + profileKey + "/" + templateCode).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template")).retrieve().bodyToMono(KakaoBizTemplateResponse.class).block();
        log.info("templateResponse = {}", templateResponse);

        return templateResponse;
    }

    public KakaoBizTemplateResponse cancelApproval(Long trackKey, String profileKey, String templateCode) {
        log.info("KAKAO TEMPLATE, CANCEL APPROVAL, TRACK KEY: {}, PROFILE KEY: {}, TEMPLATE CODE: {}", trackKey, profileKey, templateCode);
        KakaoBizTemplateResponse templateResponse = externalOAuthWebClient.put().uri(getRequestUrl(TEMPLATE_CANCEL_APPROVAL_PATH) + "/" + profileKey + "/" + templateCode).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template")).retrieve().bodyToMono(KakaoBizTemplateResponse.class).block();
        log.info("templateResponse = {}", templateResponse);

        return templateResponse;
    }

    public KakaoBizTemplateResponse dormantRelease(Long trackKey, String profileKey, String templateCode) {
        log.info("KAKAO TEMPLATE, DORMANT RELEASE, TRACK KEY: {}, PROFILE KEY: {}, TEMPLATE CODE: {}", trackKey, profileKey, templateCode);
        KakaoBizTemplateResponse templateResponse = externalOAuthWebClient.put().uri(getRequestUrl(TEMPLATE_DORMANT_PATH) + "/" + profileKey + "/" + templateCode).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template")).retrieve().bodyToMono(KakaoBizTemplateResponse.class).block();
        log.info("templateResponse = {}", templateResponse);

        return templateResponse;
    }

    public KakaoBizTemplateResponse<List<KakaoSendProfileResponse>> getAllSendProfile(Long trackKey, KakaoBizSearchSendEvent dto) {
        log.info("KAKAO TEMPLATE, ALL SEND PROFILE, TRACK KEY: {}, SEARCH DTO : {}", trackKey, dto);

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(TEMPLATE_PROFILE_SELECT_PATH)
                .queryParamIfPresent("startDate", Optional.ofNullable(dto.getStartDate()))
                .queryParamIfPresent("endDate", Optional.ofNullable(dto.getEndDate()))
                .queryParamIfPresent("sendProfileKey", Optional.ofNullable(dto.getSendProfileKey()))
                .queryParamIfPresent("page", Optional.ofNullable(dto.getPage()))
                .queryParamIfPresent("rows", Optional.ofNullable(dto.getRows()));

        builder.scheme(getBaseUrl().split("://")[0]);
        builder.host(getBaseUrl().split("://")[1]);
        URI uri = builder.build().toUri();

        KakaoBizTemplateResponse<List<KakaoSendProfileResponse>> sendProfileResponse = externalOAuthWebClient.get().uri(uri).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template"))
                .retrieve().bodyToMono(new ParameterizedTypeReference<KakaoBizTemplateResponse<List<KakaoSendProfileResponse>>>() {
                }).block();

        return sendProfileResponse;
    }

    public KakaoBizTemplateResponse<KakaoSendProfileResponse> getProfileKey(Long trackKey, String profileKey) {
        log.info("KAKAO TEMPLATE, GET SEND PROFILE, TRACK KEY: {}, PROFILE KEY: {}", trackKey, profileKey);

        KakaoBizTemplateResponse<KakaoSendProfileResponse> sendProfileResponse = externalOAuthWebClient.get().uri(getRequestUrl(TEMPLATE_PROFILE_SELECT_PATH) + "/" + profileKey).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template"))
                .retrieve().bodyToMono(new ParameterizedTypeReference<KakaoBizTemplateResponse<KakaoSendProfileResponse>>() {
                }).block();

        return sendProfileResponse;
    }

    public KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.ProfileCategory>> getAllProfileCategory(Long trackKey) {
        log.info("KAKAO TEMPLATE, GET ALL PROFILE CATEGORY, TRACK KEY: {}", trackKey);

        KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.ProfileCategory>> sendProfileResponse = externalOAuthWebClient.get().uri(getRequestUrl(TEMPLATE_PROFILE_CATEGORY_ALL_PATH)).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template"))
                .retrieve().bodyToMono(new ParameterizedTypeReference<KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.ProfileCategory>>>() {
                }).block();

        return sendProfileResponse;
    }

    public KakaoBizTemplateResponse<KakaoBizTemplateResponse.ProfileCategory> getProfileCategory(Long trackKey, String categoryCode) {
        log.info("KAKAO TEMPLATE, GET PROFILE CATEGORY, TRACK KEY: {}, CATEGORY CODE: {}", trackKey, categoryCode);

        KakaoBizTemplateResponse<KakaoBizTemplateResponse.ProfileCategory> sendProfileResponse = externalOAuthWebClient.get().uri(getRequestUrl(TEMPLATE_PROFILE_CATEGORY_CODE_PATH) + "/" + categoryCode).attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-template"))
                .retrieve().bodyToMono(new ParameterizedTypeReference<KakaoBizTemplateResponse<KakaoBizTemplateResponse.ProfileCategory>>() {
                }).block();

        return sendProfileResponse;
    }

    private String getRequestUrl(@NotNull String endPoint) {
        PlatformProperty.Platform platform = platformProperty.getPlatforms().get(PlatformType.kakao_template.name());
        Assert.notNull(platform, "PLATFORM IS NULL");

        return platform.getApiBaseUrl() + endPoint;
    }

    private String getBaseUrl() {
        PlatformProperty.Platform platform = platformProperty.getPlatforms().get(PlatformType.kakao_template.name());
        Assert.notNull(platform, "PLATFORM IS NULL");

        return platform.getApiBaseUrl();
    }
}
