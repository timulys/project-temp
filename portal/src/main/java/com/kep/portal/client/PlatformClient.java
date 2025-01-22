package com.kep.portal.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.IssueLogDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.KakaoBizDetailResponse;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.KakaoBizTalkSendResponse;
import com.kep.core.model.dto.platform.kakao.KakaoBizTemplateResponse;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.BizTalkResponseDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.SendProfileResponseDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TemplateCategoryResponseDto;
import com.kep.core.model.dto.platform.kakao.profile.KakaoSendProfileResponse;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import com.kep.core.model.exception.BizException;
import com.kep.portal.config.property.CoreProperty;
import com.kep.portal.config.property.PlatformProperty;
import com.kep.portal.config.property.TalkProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Platform API 클라아언트
 *
 * FIXME :: 통신 관련 전체 리팩토링 필요 volka
 */
@Service
@Slf4j
public class PlatformClient {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private WebClient.Builder webClientBuilder;
    @Resource
    private CoreProperty coreProperty;
    @Resource
    private PlatformProperty platformProperty;
    @Resource
    private TalkProperty talkProperty;
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 메세지 이벤트 전달 to PLATFORM
     */
    @Retryable(value = {RestClientException.class}
            , maxAttempts = 3
            , backoff = @Backoff(delay = 1000, multiplier = 2))
    public boolean message(
            @NotNull @Valid IssueDto issueDto,
            @NotNull @Valid IssueLogDto issueLogDto) throws Exception {

        return message(issueDto, Collections.singletonList(issueLogDto));
    }

    /**
     * 메세지 이벤트 전달 to PLATFORM
     */
    @Retryable(value = {RestClientException.class}
            , maxAttempts = 3
            , backoff = @Backoff(delay = 1000, multiplier = 2))
    public boolean message(
            @NotNull @Valid IssueDto issueDto,
            @NotNull @Valid List<IssueLogDto> issueLogDtos) throws Exception {

        Long trackKey = System.currentTimeMillis();
        HttpHeaders headers = getHeadersForList(issueDto, issueLogDtos, trackKey);

        List<IssuePayload> issuePayloads = new ArrayList<>();
        for (IssueLogDto issueLogDto : issueLogDtos) {
            IssuePayload issuePayload = objectMapper.readValue(issueLogDto.getPayload(), IssuePayload.class);
            issuePayloads.add(issuePayload);
        }
        HttpEntity<List<IssuePayload>> request = new HttpEntity<>(issuePayloads, headers);

        String baseUrl = coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath();
        String requestUrl = baseUrl + platformProperty.getMessagePath();

        try {
            log.info("SEND TO PLATFORM, MESSAGE, TRACK KEY: {}, HEADER: {}, BODY: {}",
                    trackKey, headers, issuePayloads);
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, String.class);
            log.info("SEND TO PLATFORM, MESSAGE, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            return true;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    /**
     * 플랫폼 설정 이벤트 전달 (ex, 카카오 상담톡 무과금 메세지)
     */
    @Retryable(value = {RestClientException.class}
            , maxAttempts = 3
            , backoff = @Backoff(delay = 1000, multiplier = 2))
    public boolean platformAnswer(
            @NotNull @Valid IssueDto issueDto,
            @NotNull IssuePayload.PlatformAnswer platformAnswer) throws Exception {

        Long trackKey = System.currentTimeMillis();
        HttpHeaders headers = getHeaders(issueDto, null, trackKey);

        IssuePayload issuePayload = new IssuePayload(platformAnswer);
        HttpEntity<IssuePayload> request = new HttpEntity<>(issuePayload, headers);

        String baseUrl = coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath();
        String requestUrl = baseUrl + platformProperty.getMessagePath();

        try {
            log.info("SEND TO PLATFORM, PLATFORM ANSWER, TRACK KEY: {}, HEADER: {}, BODY: {}",
                    trackKey, headers, issuePayload);
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, String.class);
            log.info("SEND TO PLATFORM, PLATFORM ANSWER, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            return true;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    /**
     * 세션 종료 이벤트 전달
     */
    public boolean close(
            @NotNull @Valid IssueDto issueDto) throws Exception {

        return close(issueDto, null);
    }

    /**
     * 세션 종료 이벤트 전달
     */
    @Retryable(value = {RestClientException.class}
            , maxAttempts = 3
            , backoff = @Backoff(delay = 1000, multiplier = 2))
    public boolean close(
            @NotNull @Valid IssueDto issueDto,
            @Valid List<IssueLogDto> issueLogDtos) throws Exception {

        Long trackKey = System.currentTimeMillis();
        HttpHeaders headers = getHeadersForList(issueDto, issueLogDtos, trackKey);

        List<IssuePayload> issuePayloads = new ArrayList<>();

        if (!ObjectUtils.isEmpty(issueLogDtos)) {
            for (IssueLogDto issueLogDto : issueLogDtos) {
                IssuePayload issuePayload = objectMapper.readValue(issueLogDto.getPayload(), IssuePayload.class);
                issuePayloads.add(issuePayload);
            }
        }

        HttpEntity<List<IssuePayload>> request;
        if (!ObjectUtils.isEmpty(issuePayloads)) {
            request = new HttpEntity<>(issuePayloads, headers);
        } else {
            request = new HttpEntity<>(headers);
        }

        String baseUrl = coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath();
        String requestUrl = baseUrl + platformProperty.getClosePath();

        try {
            log.info("SEND TO PLATFORM, CLOSE, TRACK KEY: {}, HEADER: {}, BODY: {}",
                    trackKey, headers, issuePayloads);
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, String.class);
            log.info("SEND TO PLATFORM, CLOSE, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            return true;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    /**
     * 봇 이력 (Async), 봇 이력 많아서 이슈될 경우 사용
     */
    @Async("platformClientExecutor")
    public CompletableFuture<List<IssuePayload>> botHistoryAsync(
            @NotNull @Valid IssueDto issueDto) throws Exception {

        return new AsyncResult<>(botHistory(issueDto)).completable();
    }

    /**
     * 봇 이력
     */
    public List<IssuePayload> botHistory(
            @NotNull @Valid IssueDto issueDto) throws Exception {

        Long trackKey = System.currentTimeMillis();
        HttpHeaders headers = getHeaders(issueDto, trackKey);

        HttpEntity<String> request = new HttpEntity<>(headers);

        String baseUrl = coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath();
        String requestUrl = baseUrl + platformProperty.getRelayPath();

        try {
            log.info("SEND TO PLATFORM, RELAY, TRACK KEY: {}, HEADER: {}",
                    trackKey, headers);
            ResponseEntity<ApiResult<List<IssuePayload>>> responseEntity = restTemplate.exchange(requestUrl,
                    HttpMethod.GET, request, new ParameterizedTypeReference<ApiResult<List<IssuePayload>>>() {
                    });
            log.info("SEND TO PLATFORM, RELAY, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), objectMapper.writeValueAsString(responseEntity.getBody()));
            if (responseEntity.getBody() != null
                    && responseEntity.getBody().getPayload() != null) {
                return responseEntity.getBody().getPayload();
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }

        return new ArrayList<>();
    }

    /**
     * 이미지 업로드
     */
    @Nullable
    public String uploadImage(
            @NotNull @Valid IssueDto issueDto,
            @NotNull UploadPlatformRequestDto uploadPlatformRequestDto) throws Exception {

        Long trackKey = System.currentTimeMillis();
        HttpHeaders headers = getHeaders(issueDto, trackKey);

        HttpEntity<UploadPlatformRequestDto> request = new HttpEntity<>(uploadPlatformRequestDto, headers);

        String baseUrl = coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath();
        String requestUrl = baseUrl + "/upload/image";

        try {
            log.info("SEND TO PLATFORM, UPLOAD IMAGE, TRACK KEY: {}, HEADER: {}, BODY: {}",
                    trackKey, headers, uploadPlatformRequestDto);
            ResponseEntity<ApiResult<String>> responseEntity = restTemplate.exchange(
                    requestUrl, HttpMethod.POST, request, new ParameterizedTypeReference<ApiResult<String>>() {
                    });
            log.info("SEND TO PLATFORM, UPLOAD IMAGE, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            // TODO: 실패, 플랫폼 규격, 네트워크 문제 등
            if (responseEntity.getBody() != null) {
                return responseEntity.getBody().getPayload();
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    /**
     * 파일 업로드
     */
    @Nullable
    public String uploadFile(
            @NotNull @Valid IssueDto issueDto,
            @NotNull UploadPlatformRequestDto uploadPlatformRequestDto) throws Exception {

        Long trackKey = System.currentTimeMillis();
        HttpHeaders headers = getHeaders(issueDto, trackKey);

        HttpEntity<UploadPlatformRequestDto> request = new HttpEntity<>(uploadPlatformRequestDto, headers);

        String baseUrl = coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath();
        String requestUrl = baseUrl + "/upload/file";

        try {
            log.info("SEND TO PLATFORM, UPLOAD FILE, TRACK KEY: {}, HEADER: {}, BODY: {}",
                    trackKey, headers, uploadPlatformRequestDto);
            ResponseEntity<ApiResult<String>> responseEntity = restTemplate.exchange(
                    requestUrl, HttpMethod.POST, request, new ParameterizedTypeReference<ApiResult<String>>() {
                    });
            log.info("SEND TO PLATFORM, UPLOAD FILE, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            // TODO: 실패, 플랫폼 규격, 네트워크 문제 등
            if (responseEntity.getBody() != null) {
                return responseEntity.getBody().getPayload();
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    private HttpHeaders getHeaders(@NotNull IssueDto issueDto, @NotNull Long trackKey) {

        return getHeaders(issueDto, null, trackKey);
    }

    private HttpHeaders getHeaders(@NotNull IssueDto issueDto, IssueLogDto issueLogDto, @NotNull Long trackKey) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Platform-Type", issueDto.getChannel().getPlatform().name());
        headers.add("X-Service-Key", issueDto.getChannel().getServiceKey());
        headers.add("X-User-Key", issueDto.getGuest().getUserKey());
        if (issueLogDto != null && issueLogDto.getId() != null) {
            headers.add("X-Event-Key", String.valueOf(issueLogDto.getId()));
        }
        headers.add("X-Track-Key", String.valueOf(trackKey));

        return headers;
    }

    private HttpHeaders getHeadersForList(@NotNull IssueDto issueDto, List<IssueLogDto> issueLogDtos, @NotNull Long trackKey) {

        if (!ObjectUtils.isEmpty(issueLogDtos)) {
            IssueLogDto issueLogDto = issueLogDtos.get(issueLogDtos.size() - 1);
            return getHeaders(issueDto, issueLogDto, trackKey);
        } else {
            return getHeaders(issueDto, null, trackKey);
        }
    }

    private HttpHeaders getKakaoBizHeaders(@NotNull Long trackKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Track-Key", String.valueOf(trackKey));

        return headers;
    }

    private HttpHeaders getKakaoBizUploadHeaders(@NotNull Long trackKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("X-Track-Key", String.valueOf(trackKey));

        return headers;
    }

    public KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> getKakaoBizTemplateInfo(String profileKey, String templateCode) {
        Long trackKey = System.currentTimeMillis();

        HttpHeaders headers = getKakaoBizHeaders(trackKey);

        HttpEntity request = new HttpEntity<>(headers);

        String baseUrl = coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath();
        String requestUrl = baseUrl + "/template/" + profileKey + "/" + templateCode;

        try {
            log.info("SEND TO PLATFORM, GET KAKAO BIZ TEMPLATE INFO, TRACK KEY: {}, HEADER: {}, PROFILE KEY: {}, TEMPLATE CODE: {}",
                    trackKey, headers, profileKey, templateCode);
            ResponseEntity<ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>> responseEntity = restTemplate.exchange(
                    requestUrl, HttpMethod.GET, request, new ParameterizedTypeReference<ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>>() {
                    });
            log.info("SEND TO PLATFORM, KAKAO BIZ TEMPLATE INFO, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            // TODO: 실패, 플랫폼 규격, 네트워크 문제 등
            if (responseEntity.getBody() != null) {
                return responseEntity.getBody().getPayload();
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return new KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>();
    }

    public KakaoBizTalkSendResponse sendBizTalk(String jsonBody, PlatformType platformType) {
        WebClient webClient = webClientBuilder.baseUrl(coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath()).build();
        ApiResult<KakaoBizTalkSendResponse> response = null;
        if (platformType == PlatformType.kakao_alert_talk) { // 알림톡
            response = webClient.post().uri("/alert-talk").headers(getHttpHeadersConsumer())
                    .bodyValue(jsonBody)
                    .retrieve().bodyToMono(new ParameterizedTypeReference<ApiResult<KakaoBizTalkSendResponse>>() {
                    }).block();
        } else if (platformType == PlatformType.kakao_friend_talk) { // 친구톡
            response = webClient.post().uri("/friend-talk").headers(getHttpHeadersConsumer())
                    .bodyValue(jsonBody)
                    .retrieve().bodyToMono(new ParameterizedTypeReference<ApiResult<KakaoBizTalkSendResponse>>() {
                    }).block();
        }
        return response.getPayload();
    }

    public KakaoBizDetailResponse getBizTalkDetail(String uid, PlatformType type) {
        WebClient webClient = webClientBuilder.baseUrl(coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath()).build();
        ApiResult<KakaoBizDetailResponse> response = null;

        if (type.equals(PlatformType.kakao_alert_talk)) {
            response = webClient.get().uri("/alert-talk/search/detail/" + uid).header("X-Track-Key", String.valueOf(System.currentTimeMillis())).retrieve().bodyToMono(new ParameterizedTypeReference<ApiResult<KakaoBizDetailResponse>>() {
            }).block();
        } else if (type.equals(PlatformType.kakao_friend_talk)) {
            response = webClient.get().uri("/friend-talk/search/detail/" + uid).header("X-Track-Key", String.valueOf(System.currentTimeMillis())).retrieve().bodyToMono(new ParameterizedTypeReference<ApiResult<KakaoBizDetailResponse>>() {
            }).block();
        }

        return response.getPayload();

    }

    private Consumer<HttpHeaders> getHttpHeadersConsumer() {
        return httpHeaders -> {
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        };
    }

    public KakaoBizTemplateResponse<List<KakaoSendProfileResponse>> getKakaoBizProfileList() {
        Long trackKey = System.currentTimeMillis();

        HttpHeaders headers = getKakaoBizHeaders(trackKey);

        HttpEntity request = new HttpEntity<>(headers);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        String baseUrl = coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath();
        // 카카오 비즈메세지 센터의 발신프로필 목록 조회 시 날짜가 입력이 안되면 당일 등록된 프로필만 조회를 해옴.
        // 따라서 2000년 1월 1일을 시작일로 endDate는 최대 오늘날짜까지 세팅이 가능하여 아래와 같이 세팅함
        String requestUrl = baseUrl + "/send-profile/select/all?startDate=2000-01-01&endDate=" + sdf.format(c.getTime()) + "&page=1&rows=100";

        try {
            log.info("SEND TO PLATFORM, GET KAKAO BIZ PROFILE LIST, TRACK KEY: {}, HEADER: {}, END DATE: {}",
                    trackKey, headers, sdf.format(c.getTime()));
            ResponseEntity<ApiResult<KakaoBizTemplateResponse<List<KakaoSendProfileResponse>>>> responseEntity = restTemplate.exchange(
                    requestUrl, HttpMethod.GET, request, new ParameterizedTypeReference<ApiResult<KakaoBizTemplateResponse<List<KakaoSendProfileResponse>>>>() {
                    });
            log.info("SEND TO PLATFORM, KAKAO BIZ PROFILE LIST, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            // TODO: 실패, 플랫폼 규격, 네트워크 문제 등
            if (responseEntity.getBody() != null) {
                return responseEntity.getBody().getPayload();
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return new KakaoBizTemplateResponse<List<KakaoSendProfileResponse>>();
    }

    /**
     * Send Profile Info (V3)
     * @param senderProfileKey
     * @return
     */
    public SendProfileResponseDto getKakaoBizProfileInfo(String senderProfileKey){
        Long trackKey = System.currentTimeMillis();
        HttpHeaders headers = getKakaoBizHeaders(trackKey);
        HttpEntity request = new HttpEntity<>(headers);

        String baseUrl = coreProperty.getTalkServiceUri() + talkProperty.getApiBasePath(); // V3
        String requestUrl = baseUrl + "/sendProfile/" + senderProfileKey;

        try {
            log.info("SEND TO PLATFORM, GET KAKAO BIZ PROFILE INFO, TRACK KEY: {}, HEADER: {}",
                    trackKey, headers);
            ResponseEntity<BizTalkResponseDto> responseEntity = restTemplate.exchange(
                    requestUrl, HttpMethod.GET, request, new ParameterizedTypeReference<BizTalkResponseDto>() {});
            log.info("SEND TO PLATFORM, KAKAO BIZ PROFILE INFO, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            if (responseEntity.getBody() != null && responseEntity.getBody().getCode().contains("200")) {
                return objectMapper.convertValue(responseEntity.getBody().getData(), SendProfileResponseDto.class);
            }
            throw new BizException(responseEntity.getBody().getMessage(), responseEntity.getBody().getCode());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return new SendProfileResponseDto();
    }

    /**
     * Template Category List (V2)
     * @return
     */
    public List<TemplateCategoryResponseDto> getKakaoBizTemplateCategoryList() {
        Long trackKey = System.currentTimeMillis();
        HttpHeaders headers = getKakaoBizHeaders(trackKey);
        HttpEntity request = new HttpEntity<>(headers);

//        String baseUrl = coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath();
//        String requestUrl = baseUrl + "/template/category/all";

        String baseUrl = coreProperty.getTalkServiceUri() + talkProperty.getApiBasePath(); // V2
        String requestUrl = baseUrl + "/template/category/all";

        try {
            log.info("SEND TO PLATFORM, GET KAKAO BIZ PROFILE LIST, TRACK KEY: {}, HEADER: {}",trackKey, headers);
            ResponseEntity<BizTalkResponseDto> responseEntity = restTemplate.exchange(
                    requestUrl, HttpMethod.GET, request, new ParameterizedTypeReference<BizTalkResponseDto>() {});
            log.info("SEND TO PLATFORM, KAKAO BIZ PROFILE LIST, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            if (responseEntity.getBody() != null && responseEntity.getBody().getCode().contains("200")) {
                return objectMapper.convertValue(responseEntity.getBody().getData(), new TypeReference<List<TemplateCategoryResponseDto>>() {});
            }
            throw new BizException(responseEntity.getBody().getMessage(), responseEntity.getBody().getCode());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return Collections.emptyList();
    }

    public KakaoBizTemplateResponse uploadAlertTemplateImage(UploadPlatformRequestDto uploadDto, String target) throws Exception {
        Long trackKey = System.currentTimeMillis();

        HttpHeaders headers = getKakaoBizUploadHeaders(trackKey);

        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("sourceFile", uploadDto.getSourceFile().getResource());

        HttpEntity<MultiValueMap> request = new HttpEntity<>(multiValueMap, headers);

        String baseUrl = coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath();
        String requestUrl = baseUrl + "/template/upload/image";

        if ("highlight".equals(target)) {
            requestUrl += "/item-highlight";
        }

        try {
            log.info("SEND TO PLATFORM, UPLOAD TEMPLATE IMAGE, TRACK KEY: {}, HEADER: {}, BODY: {}",
                    trackKey, headers, uploadDto);
            ResponseEntity<ApiResult<KakaoBizTemplateResponse>> responseEntity = restTemplate.exchange(
                    requestUrl, HttpMethod.POST, request, new ParameterizedTypeReference<ApiResult<KakaoBizTemplateResponse>>() {
                    });
            log.info("SEND TO PLATFORM, UPLOAD TEMPLATE IMAGE, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            // TODO: 실패, 플랫폼 규격, 네트워크 문제 등
            if (responseEntity.getBody() != null) {
                return responseEntity.getBody().getPayload();
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return new KakaoBizTemplateResponse();
    }

    /**
     * Get Kakao BizTalk Service Client Id(V2)
     * FIXME : 신규 버전으로 Return도 개선할 것(DTO)
     * @return
     */
    public String selectKakaoTemplateClientId(){
        Long trackKey = System.currentTimeMillis();
        HttpHeaders headers = getKakaoBizHeaders(trackKey);
        HttpEntity request = new HttpEntity<>(headers);

        String baseUrl = coreProperty.getTalkServiceUri() + talkProperty.getApiBasePath(); // v2
        String requestUrl = baseUrl + "/template/clientId";

        try {
            log.info("SEND TO PLATFORM, SELECT KAKAO TEMPLATE CLIENT ID, TRACK KEY: {}, HEADER: {}",
                    trackKey, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    requestUrl, HttpMethod.GET, request, new ParameterizedTypeReference<String>() {});
            log.info("SEND TO PLATFORM, SELECT KAKAO TEMPLATE CLIENT ID, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            // TODO: 실패, 플랫폼 규격, 네트워크 문제 등
            if (responseEntity.getBody() != null) {
                return responseEntity.getBody();
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return new String();
    }

    /**
     * Save BizMessage Template(V2)
     * @param profileKey
     * @param payload
     * @return
     * @throws Exception
     */
    public BizTalkResponseDto<KakaoBizMessageTemplatePayload> saveKakaoBizTemplate(String profileKey, KakaoBizMessageTemplatePayload payload) throws Exception {
        Long trackKey = System.currentTimeMillis();
        HttpHeaders headers = getKakaoBizHeaders(trackKey);

        payload.setSenderKey(profileKey);
        payload.setSenderKeyType("S");

        HttpEntity<KakaoBizMessageTemplatePayload> request = new HttpEntity<>(payload, headers);

        String baseUrl = coreProperty.getTalkServiceUri() + talkProperty.getApiBasePath(); // V2
        String requestUrl = baseUrl + "/template";

        HttpMethod method = HttpMethod.POST;

        try {
            if (!ObjectUtils.isEmpty(payload.getNewTemplateCode())) {
                // 수정이므로 PUT으로 호출
                method = HttpMethod.PUT;
            }

            log.info("SEND TO PLATFORM, SAVE KAKAO BIZ TEMPLATE INFO, TRACK KEY: {}, HEADER: {}, METHOD: {}, SAVE INFO: {}, PROFILE KEY: {}, BODY: {}",
                    trackKey, headers, method, (method.equals(HttpMethod.POST) ? "CREATE" : "MODIFY"), profileKey, payload);
            ResponseEntity<BizTalkResponseDto<KakaoBizMessageTemplatePayload>> responseEntity = restTemplate.exchange(
                    requestUrl, method, request, new ParameterizedTypeReference<BizTalkResponseDto<KakaoBizMessageTemplatePayload>>() {});
            log.info("SEND TO PLATFORM, SAVE KAKAO BIZ TEMPLATE INFO, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            if (responseEntity.getBody() != null && responseEntity.getBody().getCode().contains("200")) {
                return responseEntity.getBody(); // TODO : return Value를 하나의 방식으로 통일해야 함. responseEntity.getBody()인지 .getData()인지
            }
            throw new BizException(responseEntity.getBody().getMessage(), responseEntity.getBody().getCode());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return new BizTalkResponseDto<KakaoBizMessageTemplatePayload>();
    }

    public KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> cancelKakaoBizTemplateRequest(String profileKey, String templateCode) {
        Long trackKey = System.currentTimeMillis();

        HttpHeaders headers = getKakaoBizHeaders(trackKey);

        HttpEntity request = new HttpEntity<>(headers);

        String baseUrl = coreProperty.getTalkServiceUri() + talkProperty.getApiBasePath();
        String requestUrl = String.format("%s/%s?profileKey=%s&templateCode=%s", baseUrl, "template/cancel/request", profileKey, templateCode);
//        String baseUrl = coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath();
//        String requestUrl = baseUrl + "/template/cancel/request/" + profileKey + "/" + templateCode;

        try {
            log.info("SEND TO PLATFORM, CANCEL REQUEST KAKAO BIZ TEMPLATE, TRACK KEY: {}, HEADER: {}, PROFILE KEY: {}, TEMPLATE CODE: {}",
                    trackKey, headers, profileKey, templateCode);
            ResponseEntity<ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>> responseEntity = restTemplate.exchange(
                    requestUrl, HttpMethod.PUT, request, new ParameterizedTypeReference<ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>>() {
                    });
            log.info("SEND TO PLATFORM, CANCEL REQUEST KAKAO BIZ TEMPLATE, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            // TODO: 실패, 플랫폼 규격, 네트워크 문제 등
            if (responseEntity.getBody() != null) {
                return responseEntity.getBody().getPayload();
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return new KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>();
    }

    public KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> deleteKakaoBizTemplate(String profileKey, String templateCode) {
        Long trackKey = System.currentTimeMillis();

        HttpHeaders headers = getKakaoBizHeaders(trackKey);

        HttpEntity request = new HttpEntity<>(headers);

        String baseUrl = coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath();
        String requestUrl = baseUrl + "/template/" + profileKey + "/" + templateCode;

        try {
            log.info("SEND TO PLATFORM, DELETE KAKAO BIZ TEMPLATE INFO, TRACK KEY: {}, HEADER: {}, PROFILE KEY: {}, TEMPLATE CODE: {}",
                    trackKey, headers, profileKey, templateCode);
            ResponseEntity<ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>> responseEntity = restTemplate.exchange(
                    requestUrl, HttpMethod.DELETE, request, new ParameterizedTypeReference<ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>>() {
                    });
            log.info("SEND TO PLATFORM, DELETE KAKAO BIZ TEMPLATE INFO, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            // TODO: 실패, 플랫폼 규격, 네트워크 문제 등
            if (responseEntity.getBody() != null) {
                return responseEntity.getBody().getPayload();
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return new KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>();
    }

    public BizTalkResponseDto uploadFriendTemplateImage(UploadPlatformRequestDto uploadDto) throws Exception {
        Long trackKey = System.currentTimeMillis();

        HttpHeaders headers = getKakaoBizUploadHeaders(trackKey);

        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("sourceFile", uploadDto.getSourceFile().getResource());

        HttpEntity<MultiValueMap> request = new HttpEntity<>(multiValueMap, headers);

//        String baseUrl = coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath();
//        String requestUrl = baseUrl + "/friend-talk/upload/image";
        String baseUrl = coreProperty.getTalkServiceUri() + talkProperty.getApiBasePath();
        String requestUrl = baseUrl + "/friendtalk/upload";

        try {
            log.info("SEND TO PLATFORM, UPLOAD TEMPLATE IMAGE, TRACK KEY: {}, HEADER: {}, BODY: {}",
                    trackKey, headers, uploadDto);
            ResponseEntity<BizTalkResponseDto> responseEntity = restTemplate.exchange(
                    requestUrl, HttpMethod.POST, request, new ParameterizedTypeReference<BizTalkResponseDto>() {
                    });
            log.info("SEND TO PLATFORM, UPLOAD TEMPLATE IMAGE, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
                    trackKey, responseEntity.getStatusCode(), responseEntity.getBody());
            // TODO: 실패, 플랫폼 규격, 네트워크 문제 등
            if (responseEntity.getBody() != null) {
                return responseEntity.getBody();
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return new BizTalkResponseDto();
    }
}
