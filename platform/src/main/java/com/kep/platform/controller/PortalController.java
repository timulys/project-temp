package com.kep.platform.controller;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.event.PlatformEventDto;
import com.kep.core.model.dto.event.PlatformEventType;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.*;
import com.kep.core.model.dto.platform.kakao.profile.KakaoSendProfileResponse;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import com.kep.core.model.type.QueryParam;
import com.kep.core.util.TimeUtils;
import com.kep.platform.model.dto.KakaoCounselSendEvent;
import com.kep.platform.service.kakao.alert.KakaoAlertTalkService;
import com.kep.platform.service.kakao.alert.KakaoBizTalkTemplateService;
import com.kep.platform.service.kakao.counsel.KakaoCounselCenterService;
import com.kep.platform.service.kakao.counsel.KakaoCounselService;
import com.kep.platform.service.SendToPlatformProducer;
import com.kep.platform.service.kakao.friend.KakaoFriendTalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * API from Portal
 */
@Tag(name = "포탈 수신 API", description = "/api/v1/counsel-portal")
@RestController
@RequestMapping("/api/v1/counsel-portal")
//@RequestMapping("/api/v1/portal")
@Slf4j
public class PortalController {

    @Resource
    private SendToPlatformProducer producer;
    @Resource
    private KakaoCounselService kakaoCounselService;
    @Resource
    private KakaoCounselCenterService kakaoCounselCenterService;
    @Resource
    private KakaoAlertTalkService kakaoAlertTalkService;
    @Resource
    private KakaoFriendTalkService kakaoFriendTalkService;
    @Resource
    private KakaoBizTalkTemplateService kakaoBizTalkTemplateService;
    @Resource
    private OAuth2ClientProperties oAuth2ClientProperties;

    /**
     * 메세지 송신 요청
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "메시지 송신 요청")
    @PostMapping("/message")
    public ResponseEntity<ApiResult<String>> message(
            @Parameter(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template, legacy_web, legacy_app , kakao_counsel_center)", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Platform-Type") @NotNull PlatformType platformType,
            @Parameter(description = "서비스 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Service-Key") @NotEmpty String serviceKey,
            @Parameter(description = "유저키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-User-Key") @NotEmpty String userKey,
            @Parameter(description = "이벤트 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Event-Key") @NotEmpty String eventKey,
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @RequestBody String requestBody) {

        log.info("PORTAL, MESSAGE, TRACK KEY: {}, PLATFORM: {}, SERVICE: {}, USER: {}, BODY: {}",
                trackKey, platformType, serviceKey, userKey, requestBody);

        PlatformEventDto platformEventDto = PlatformEventDto.builder()
                .platformEventType(PlatformEventType.MESSAGE)
                .platformType(platformType)
                .serviceKey(serviceKey)
                .userKey(userKey)
                .eventKey(eventKey)
                .payload(requestBody)
                .created(TimeUtils.toZonedDateTime(trackKey))
                .build();

        try {
            producer.sendMessage(platformEventDto);
            return new ResponseEntity<>(new ApiResult<>(ApiResultCode.succeed), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("PORTAL, MESSAGE, TRACK KEY: {}, PLATFORM: {}, SERVICE: {}, USER: {}, BODY: {}, ERROR: {}",
                    trackKey, platformType, serviceKey, userKey, requestBody, e.getLocalizedMessage(), e);
            return new ResponseEntity<>(new ApiResult<>(ApiResultCode.failed), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * 종료 요청
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "종료 요청")
    @PostMapping("/close")
    public ResponseEntity<ApiResult<String>> close(
            @Parameter(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template, legacy_web, legacy_app , kakao_counsel_center)", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Platform-Type") @NotNull PlatformType platformType,
            @Parameter(description = "서비스 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Service-Key") @NotEmpty String serviceKey,
            @Parameter(description = "유저키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-User-Key") @NotEmpty String userKey,
            @Parameter(description = "이벤트 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Event-Key", required = false) String eventKey,
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @RequestBody(required = false) String requestBody) {

        log.info("PORTAL, CLOSE, TRACK KEY: {}, PLATFORM: {}, SERVICE: {}, USER: {}, BODY: {}",
                trackKey, platformType, serviceKey, userKey, requestBody);

        PlatformEventDto platformEventDto = PlatformEventDto.builder()
                .platformEventType(PlatformEventType.CLOSE)
                .platformType(platformType)
                .serviceKey(serviceKey)
                .userKey(userKey)
                .eventKey(eventKey)
                .payload(requestBody)
                .created(TimeUtils.toZonedDateTime(trackKey))
                .build();

        try {
            producer.sendMessage(platformEventDto);
            return new ResponseEntity<>(new ApiResult<>(ApiResultCode.succeed), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("PORTAL, CLOSE, TRACK KEY: {}, PLATFORM: {}, SERVICE: {}, USER: {}, BODY: {}, ERROR: {}",
                    trackKey, platformType, serviceKey, userKey, requestBody, e.getLocalizedMessage(), e);
            return new ResponseEntity<>(new ApiResult<>(ApiResultCode.failed), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * 봇 대화 이력 요청
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "봇 대화 이력 요청")
    @GetMapping("/relay")
    public ResponseEntity<ApiResult<List<IssuePayload>>> relay(
            @Parameter(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template, legacy_web, legacy_app , kakao_counsel_center)", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Platform-Type") @NotNull PlatformType platformType,
            @Parameter(description = "서비스 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Service-Key") @NotEmpty String serviceKey,
            @Parameter(description = "유저키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-User-Key") @NotEmpty String userKey,
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @RequestBody(required = false) String requestBody) {

        log.info("PORTAL, RELAY, TRACK KEY: {}, PLATFORM: {}, SERVICE: {}, USER: {}, BODY: {}",
                trackKey, platformType, serviceKey, userKey, requestBody);

        KakaoCounselSendEvent event = KakaoCounselSendEvent.builder()
                .senderKey(serviceKey)
                .userKey(userKey)
                .build();

        try {
            List<IssuePayload> history = kakaoCounselService.relay(event, trackKey);
            ApiResult<List<IssuePayload>> response = ApiResult.<List<IssuePayload>>builder()
                    .code(ApiResultCode.succeed)
                    .payload(history)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("PORTAL, RELAY, TRACK KEY: {}, PLATFORM: {}, SERVICE: {}, USER: {}, BODY: {}, ERROR: {}",
                    trackKey, platformType, serviceKey, userKey, requestBody, e.getLocalizedMessage(), e);
            return new ResponseEntity<>(new ApiResult<>(ApiResultCode.failed), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * 이미지 업로드
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "이미지 업로드")
    @PostMapping("/upload/image")
    public ResponseEntity<ApiResult<String>> uploadImage(
            @Parameter(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template, legacy_web, legacy_app , kakao_counsel_center)", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Platform-Type") @NotNull PlatformType platformType,
            @Parameter(description = "서비스 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Service-Key") @NotEmpty String serviceKey,
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @RequestBody UploadPlatformRequestDto uploadPlatformRequestDto) {

        log.info("PORTAL, UPLOAD, IMAGE, TRACK KEY: {}, PLATFORM: {}, SERVICE: {}, BODY: {}",
                trackKey, platformType, serviceKey, uploadPlatformRequestDto);
        Assert.isTrue(PlatformType.kakao_counsel_talk.equals(platformType), "platform not support");

        try {
            String uploadUrl = kakaoCounselService.uploadImage(uploadPlatformRequestDto, serviceKey, trackKey);
            ApiResult<String> response = ApiResult.<String>builder()
                    .code(ApiResultCode.succeed)
                    .payload(uploadUrl)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("PORTAL, UPLOAD, IMAGE, TRACK KEY: {}, PLATFORM: {}, SERVICE: {}, BODY: {}, ERROR: {}",
                    trackKey, platformType, serviceKey, uploadPlatformRequestDto, e.getLocalizedMessage(), e);
            return new ResponseEntity<>(new ApiResult<>(ApiResultCode.failed), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * 파일 업로드
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "파일 업로드")
    @PostMapping("/upload/file")
    public ResponseEntity<ApiResult<String>> uploadFile(
            @Parameter(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template, legacy_web, legacy_app , kakao_counsel_center)", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Platform-Type") @NotNull PlatformType platformType,
            @Parameter(description = "서비스 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Service-Key") @NotEmpty String serviceKey,
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @RequestBody UploadPlatformRequestDto uploadPlatformRequestDto) {

        log.info("PORTAL, UPLOAD, FILE, TRACK KEY: {}, PLATFORM: {}, SERVICE: {}, BODY: {}",
                trackKey, platformType, serviceKey, uploadPlatformRequestDto);
        Assert.isTrue(PlatformType.kakao_counsel_talk.equals(platformType), "platform not support");

        try {
            String uploadUrl = kakaoCounselService.uploadFile(uploadPlatformRequestDto, serviceKey, trackKey);
            ApiResult<String> response = ApiResult.<String>builder()
                    .code(ApiResultCode.succeed)
                    .payload(uploadUrl)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("PORTAL, UPLOAD, FILE, TRACK KEY: {}, PLATFORM: {}, SERVICE: {}, BODY: {}, ERROR: {}",
                    trackKey, platformType, serviceKey, uploadPlatformRequestDto, e.getLocalizedMessage(), e);
            return new ResponseEntity<>(new ApiResult<>(ApiResultCode.failed), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Tag(name = "포탈 수신 API")
    @Operation(summary = "메시지 타입에 해당하는 시스템 메시지 조회 ")
    @GetMapping("/sm/{messageType}")
    public ResponseEntity<ApiResult<String>> getSystemMessage(
            @Parameter(description = "서비스 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Service-Key") @NotEmpty String serviceKey,
            @Parameter(description = "메시지 타입", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "messageType") String messageType
    ) throws Exception {
        try {
            String payload = kakaoCounselCenterService.getSystemMessage(serviceKey, messageType);
            ApiResult<String> response = ApiResult.<String>builder()
                    .code(ApiResultCode.succeed)
                    .payload(payload)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResult<String> response = ApiResult.<String>builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-SA-007-SM-PLATFORM>>");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Tag(name = "포탈 수신 API")
    @Operation(summary = "시스템 메시지 조회")
    @GetMapping("/sm")
    public ResponseEntity<ApiResult<List<IssuePayload>>> getSystemMessage(
            @Parameter(description = "서비스 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Service-Key") @NotEmpty String serviceKey) {

        List<IssuePayload> payloads = kakaoCounselCenterService.systemMessage(serviceKey);
        ApiResult<List<IssuePayload>> response = ApiResult.<List<IssuePayload>>builder()
                .code(ApiResultCode.succeed)
                .payload(payloads)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 클라이언트 ID
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "클라이언트 ID 조회")
    @GetMapping("selectKakaoTemplateClientId")
    public ResponseEntity<ApiResult<String>> selectKakaoTemplateClientId(
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey){
        Map<String, OAuth2ClientProperties.Registration> registrations = oAuth2ClientProperties.getRegistration();
        OAuth2ClientProperties.Registration registration = registrations.get("kakao-template");

        ApiResult<String> response = ApiResult.<String>builder()
                .code(ApiResultCode.succeed)
                .payload(registration.getClientId())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 알림톡 API
     */

//    @Tag(name = "포탈 수신 API")
//    @PostMapping("/alert-talk")
//    public ResponseEntity<ApiResult<KakaoBizTalkSendResponse>> sendAlertTalk(
//            @RequestBody KakaoAlertSendEvent dto,
//            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey) {
//
//        KakaoBizTalkSendResponse send = kakaoAlertTalkService.send(dto, trackKey);
//        ApiResult<KakaoBizTalkSendResponse> result = ApiResult.<KakaoBizTalkSendResponse>builder()
//                .code(ApiResultCode.succeed)
//                .payload(send)
//                .build();
//
//        return new ResponseEntity<>(result, HttpStatus.CREATED);
//    }

    @Tag(name = "포탈 수신 API")
    @Operation(summary = "알림톡 발송")
    @PostMapping("/alert-talk")
    public ResponseEntity<ApiResult<KakaoBizTalkSendResponse>> sendAlertTalk(
            @RequestBody String alertEventBody
    ) {
        PlatformEventDto platformEventDto = PlatformEventDto.builder()
                .platformType(PlatformType.kakao_alert_talk)
                .platformEventType(PlatformEventType.MESSAGE)
                .payload(alertEventBody)
                .build();

        producer.sendMessage(platformEventDto);

        ApiResult<KakaoBizTalkSendResponse> result = ApiResult.<KakaoBizTalkSendResponse>builder()
                .code(ApiResultCode.succeed)
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Tag(name = "포탈 수신 API")
    @Operation(summary = "알림톡 결과 조회")
    @GetMapping("/alert-talk")
    public ResponseEntity<ApiResult<KakaoBizTalkSendResponse>> getAlertTalkResults(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey
    ) {
        KakaoBizTalkSendResponse result = kakaoAlertTalkService.result(trackKey);
        ApiResult<KakaoBizTalkSendResponse> response = ApiResult.<KakaoBizTalkSendResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(result)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Tag(name = "포탈 수신 API")
    @Operation(summary = "알림톡 리포트 그룹별 완료 처리")
    @GetMapping("/alert-talk/complete/{reportGroupNo}")
    public ResponseEntity<ApiResult<KakaoBizTalkSendResponse>> getAlertTalkComplete(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @Parameter(description = "리포트 그룹 번호", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "reportGroupNo") String reportGroupNo
    ) {
        KakaoBizTalkSendResponse complete = kakaoAlertTalkService.complete(trackKey, reportGroupNo);
        ApiResult<KakaoBizTalkSendResponse> response = ApiResult.<KakaoBizTalkSendResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(complete)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Tag(name = "포탈 수신 API")
    @Operation(summary = "알림톡 검색")
    @GetMapping("/alert-talk/search")
    public ResponseEntity<ApiResult<KakaoBizSearchResponse>> getAlertTalkSearch(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @ParameterObject @QueryParam KakaoBizSearchSendEvent dto
    ) {
        KakaoBizSearchResponse search = kakaoAlertTalkService.search(trackKey, dto);
        ApiResult<KakaoBizSearchResponse> response = ApiResult.<KakaoBizSearchResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(search)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Tag(name = "포탈 수신 API")
    @Operation(summary = "알림톡 상세 검색")
    @GetMapping("/alert-talk/search/detail/{uid}")
    public ResponseEntity<ApiResult<KakaoBizDetailResponse>> getAlertTalkDetail(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @Parameter(description = "", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "uid") String uid

    ) {
        KakaoBizDetailResponse search = kakaoAlertTalkService.searchDetail(trackKey, uid);
        ApiResult<KakaoBizDetailResponse> response = ApiResult.<KakaoBizDetailResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(search)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 친구톡 API
     */

//    @Tag(name = "포탈 수신 API")
//    @PostMapping("/friend-talk")
//    public ResponseEntity<ApiResult<KakaoBizTalkSendResponse>> sendFriendTalk(
//            @Valid @RequestBody KakaoFriendSendEvent dto,
//            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey) {
//
//        KakaoBizTalkSendResponse send = kakaoFriendTalkService.send(dto, trackKey);
//        ApiResult<KakaoBizTalkSendResponse> result = ApiResult.<KakaoBizTalkSendResponse>builder()
//                .code(ApiResultCode.succeed)
//                .payload(send)
//                .build();
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }

    @Tag(name = "포탈 수신 API")
    @Operation(summary = "친구톡 발송")
    @PostMapping("/friend-talk")
    public ResponseEntity<ApiResult<KakaoBizTalkSendResponse>> sendFriendTalkQueue(
            @RequestBody String friendEventBody
    ) {
        PlatformEventDto platformEventDto = PlatformEventDto.builder()
                .platformType(PlatformType.kakao_friend_talk)
                .platformEventType(PlatformEventType.MESSAGE)
                .payload(friendEventBody)
                .build();

        producer.sendMessage(platformEventDto);

        ApiResult<KakaoBizTalkSendResponse> result = ApiResult.<KakaoBizTalkSendResponse>builder()
                .code(ApiResultCode.succeed)
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Tag(name = "포탈 수신 API")
    @Operation(summary = "친구톡 검색")
    @GetMapping("/friend-talk/search")
    public ResponseEntity<ApiResult<KakaoBizSearchResponse>> get(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @ParameterObject @QueryParam KakaoBizSearchSendEvent dto
    ) {
        KakaoBizSearchResponse result = kakaoFriendTalkService.search(trackKey, dto);
        ApiResult<KakaoBizSearchResponse> response = ApiResult.<KakaoBizSearchResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(result)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Tag(name = "포탈 수신 API")
    @Operation(summary = "친구톡 상세 검색")
    @GetMapping("/friend-talk/search/detail/{uid}")
    public ResponseEntity<ApiResult<KakaoBizDetailResponse>> getFriendTalkDetail(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @PathVariable(value = "uid") String uid
    ) {
        KakaoBizDetailResponse detail = kakaoFriendTalkService.detail(trackKey, uid);
        ApiResult<KakaoBizDetailResponse> response = ApiResult.<KakaoBizDetailResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(detail)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 친구톡 이미지 업로드
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "친구톡 이미지 업로드")
    @PostMapping("/friend-talk/upload/image")
    public ResponseEntity<ApiResult<KakaoBizTalkSendResponse>> postUploadImage(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            UploadPlatformRequestDto dto
    ) {
        KakaoBizTalkSendResponse talkSendResponse = kakaoFriendTalkService.uploadImage(trackKey, dto);
        ApiResult<KakaoBizTalkSendResponse> response = ApiResult.<KakaoBizTalkSendResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(talkSendResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /////////////////////////////////////////////////////////////
    // 템플릿 API
    /////////////////////////////////////////////////////////////

    /**
     * 템플릿 카테고리 리스트 조회
     * @param trackKey
     * @return
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "템플릿 카테고리 리스트 조회")
    @GetMapping("/template/category/all")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.TemplateCategory>>>> getCategory(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey) {
        KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.TemplateCategory>> category = kakaoBizTalkTemplateService.category(trackKey);
        ApiResult<KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.TemplateCategory>>> response = ApiResult.<KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.TemplateCategory>>>builder()
                .code(ApiResultCode.succeed)
                .payload(category)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 템플릿 카테고리 조회
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "템플릿 카테고리 조회")
    @GetMapping("/template/category/{categoryCode}")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse<KakaoBizTemplateResponse.TemplateCategory>>> getCategoryCode(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @Parameter(description = "카테고리 코드", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "categoryCode") String categoryCode) {
        KakaoBizTemplateResponse<KakaoBizTemplateResponse.TemplateCategory> category = kakaoBizTalkTemplateService.getCategoryCode(trackKey, categoryCode);
        ApiResult<KakaoBizTemplateResponse<KakaoBizTemplateResponse.TemplateCategory>> response = ApiResult.<KakaoBizTemplateResponse<KakaoBizTemplateResponse.TemplateCategory>>builder()
                .code(ApiResultCode.succeed)
                .payload(category)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 템플릿 등록
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "템플릿 등록")
    @PostMapping("/template/{profileKey}")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>> postTemplate(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @RequestBody KakaoBizMessageTemplatePayload dto,
            @Parameter(description = "프로필 키", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "profileKey") String profileKey) {
        KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> kakaoTemplateCategoryResponse = kakaoBizTalkTemplateService.create(trackKey, dto, profileKey);
        ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>> response = ApiResult.<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>builder()
                .code(ApiResultCode.succeed)
                .payload(kakaoTemplateCategoryResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 템플릿 이미지 업로드
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "템플릿 이미지 업로드")
    @PostMapping("/template/upload/image")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse>> postTemplate(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            UploadPlatformRequestDto dto
    ) {
        KakaoBizTemplateResponse kakaoBizTemplateResponse = kakaoBizTalkTemplateService.uploadImage(trackKey, dto);
        ApiResult<KakaoBizTemplateResponse> response = ApiResult.<KakaoBizTemplateResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(kakaoBizTemplateResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 템플릿 하이라이트 이미지 업로드
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "템플릿 하이라이트 이미지 업로드")
    @PostMapping("/template/upload/image/item-highlight")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse>> postHighlightTemplate(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            UploadPlatformRequestDto dto
    ) {
        KakaoBizTemplateResponse kakaoBizTemplateResponse = kakaoBizTalkTemplateService.highlightUploadImage(trackKey, dto);
        ApiResult<KakaoBizTemplateResponse> response = ApiResult.<KakaoBizTemplateResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(kakaoBizTemplateResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 템플릿 수정
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "템플릿 수정")
    @PutMapping("/template/{profileKey}")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>> putTemplate(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @Parameter(description = "프로필 키", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "profileKey") String profileKey,
            @RequestBody KakaoBizMessageTemplatePayload dto
    ) {
        KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> kakaoTemplateCategoryResponse = kakaoBizTalkTemplateService.modify(trackKey, profileKey, dto);
        ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>> response = ApiResult.<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>builder()
                .code(ApiResultCode.succeed)
                .payload(kakaoTemplateCategoryResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 템플릿 삭제
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "템플릿 삭제")
    @DeleteMapping("/template/{profileKey}/{templateCode}")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>> deleteTemplate(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @Parameter(description = "프로필 키", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "profileKey") String profileKey,
            @Parameter(description = "템플릿 코드", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "templateCode") String templateCode
    ) {
        KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> kakaoTemplateCategoryResponse =
                kakaoBizTalkTemplateService.remove(trackKey, profileKey, templateCode);
        ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>> response = ApiResult.<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>builder()
                .code(ApiResultCode.succeed)
                .payload(kakaoTemplateCategoryResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 템플릿 조회
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "템플릿 조회")
    @GetMapping("/template/{senderKey}/{templateCode}")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>> selectTemplate(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @Parameter(description = "발신프로필 키", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "senderKey") String senderKey,
            @Parameter(description = "템플릿 코드", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "templateCode") String templateCode) {

        KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> kakaoTemplateCategoryResponse =
                kakaoBizTalkTemplateService.select(trackKey, senderKey, templateCode);

        ApiResult<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>> response = ApiResult.<KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>>builder()
                .code(ApiResultCode.succeed)
                .payload(kakaoTemplateCategoryResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 템플릿 리스트 조회
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "템플릿 리스트 조회")
    @GetMapping("/template/all/{senderKey}")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse<List<KakaoBizMessageTemplatePayload>>>> getSelectAllTemplate(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @ParameterObject @QueryParam KakaoBizSearchSendEvent dto,
            @Parameter(description = "프로필 키", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "senderKey") String senderKey) {

        KakaoBizTemplateResponse<List<KakaoBizMessageTemplatePayload>> templateResponse =
                kakaoBizTalkTemplateService.selectAll(trackKey, senderKey, dto);

        ApiResult<KakaoBizTemplateResponse<List<KakaoBizMessageTemplatePayload>>> response = ApiResult.<KakaoBizTemplateResponse<List<KakaoBizMessageTemplatePayload>>>builder()
                .code(ApiResultCode.succeed)
                .payload(templateResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 변경된 템플릿 조회
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "변경된 템플릿 조회")
    @GetMapping("/template/last-modify/{profileKey}")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse<List<KakaoBizMessageTemplatePayload>>>> lastModifyTemplate(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @Parameter(description = "프로필 키", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "profileKey") String profileKey
    ) {
        KakaoBizTemplateResponse<List<KakaoBizMessageTemplatePayload>> kakaoTemplateCategoryResponse = kakaoBizTalkTemplateService.lastModified(trackKey, profileKey);
        ApiResult<KakaoBizTemplateResponse<List<KakaoBizMessageTemplatePayload>>> response = ApiResult.<KakaoBizTemplateResponse<List<KakaoBizMessageTemplatePayload>>>builder()
                .code(ApiResultCode.succeed)
                .payload(kakaoTemplateCategoryResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 템플릿 검수요청 취소
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "템플릿 검수요청 취소")
    @PutMapping("/template/cancel/request/{profileKey}/{templateCode}")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse>> cancelRequest(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @Parameter(description = "프로필 키", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "profileKey") String profileKey,
            @Parameter(description = "템플릿 코드", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "templateCode") String templateCode
    ) {
        KakaoBizTemplateResponse kakaoBizTemplateResponse = kakaoBizTalkTemplateService.cancelRequest(trackKey, profileKey, templateCode);
        ApiResult<KakaoBizTemplateResponse> response = ApiResult.<KakaoBizTemplateResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(kakaoBizTemplateResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 템플릿 승인 취소
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "템플릿 승인 취소")
    @PutMapping("/template/cancel/approval/{profileKey}/{templateCode}")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse>> cancelApproval(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @Parameter(description = "프로필 키", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "profileKey") String profileKey,
            @Parameter(description = "템플릿 코드", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "templateCode") String templateCode
    ) {
        KakaoBizTemplateResponse kakaoBizTemplateResponse = kakaoBizTalkTemplateService.cancelApproval(trackKey, profileKey, templateCode);
        ApiResult<KakaoBizTemplateResponse> response = ApiResult.<KakaoBizTemplateResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(kakaoBizTemplateResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 템플릿 휴면 해제
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "템플릿 휴면 해제")
    @PutMapping("/template/dormant/release/{profileKey}/{templateCode}")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse>> dormantRelease(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @Parameter(description = "프로필 키", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "profileKey") String profileKey,
            @Parameter(description = "템플릿 코드", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "templateCode") String templateCode
    ) {
        KakaoBizTemplateResponse kakaoBizTemplateResponse = kakaoBizTalkTemplateService.dormantRelease(trackKey, profileKey, templateCode);
        ApiResult<KakaoBizTemplateResponse> response = ApiResult.<KakaoBizTemplateResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(kakaoBizTemplateResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 발신프로필 관리 API
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "발신프로필 관리 API")
    @GetMapping("/send-profile/select/all")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse<List<KakaoSendProfileResponse>>>> getAllSendProfile(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @ParameterObject @QueryParam KakaoBizSearchSendEvent dto
    ) {
        KakaoBizTemplateResponse<List<KakaoSendProfileResponse>> kakaoBizTemplateResponse = kakaoBizTalkTemplateService.getAllSendProfile(trackKey, dto);
        ApiResult<KakaoBizTemplateResponse<List<KakaoSendProfileResponse>>> response = ApiResult.<KakaoBizTemplateResponse<List<KakaoSendProfileResponse>>>builder()
                .code(ApiResultCode.succeed)
                .payload(kakaoBizTemplateResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 발신프로필 조회
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "발신프로필 조회")
    @GetMapping("/send-profile/select/{profileKey}")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse<KakaoSendProfileResponse>>> getSendProfile(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @Parameter(description = "프로필 키", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "profileKey") String profileKey
    ) {
        KakaoBizTemplateResponse<KakaoSendProfileResponse> kakaoBizTemplateResponse = kakaoBizTalkTemplateService.getProfileKey(trackKey, profileKey);
        ApiResult<KakaoBizTemplateResponse<KakaoSendProfileResponse>> response = ApiResult.<KakaoBizTemplateResponse<KakaoSendProfileResponse>>builder()
                .code(ApiResultCode.succeed)
                .payload(kakaoBizTemplateResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 발신프로필 카테고리 리스트 조회
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "발신프로필 카테고리 리스트 조회")
    @GetMapping("/send-profile/category/select/all")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.ProfileCategory>>>> getAllProfileCategory(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey
    ) {
        KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.ProfileCategory>> kakaoBizTemplateResponse = kakaoBizTalkTemplateService.getAllProfileCategory(trackKey);
        ApiResult<KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.ProfileCategory>>> response = ApiResult.<KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.ProfileCategory>>>builder()
                .code(ApiResultCode.succeed)
                .payload(kakaoBizTemplateResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 발신프로필 카테고리 조회
     */
    @Tag(name = "포탈 수신 API")
    @Operation(summary = "발신프로필 카테고리 조회")
    @GetMapping("/send-profile/category/select/{categoryCode}")
    public ResponseEntity<ApiResult<KakaoBizTemplateResponse<KakaoBizTemplateResponse.ProfileCategory>>> getProfileCategory(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") @NotNull Long trackKey,
            @Parameter(description = "카테고리 코드", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "categoryCode") String categoryCode
    ) {
        KakaoBizTemplateResponse<KakaoBizTemplateResponse.ProfileCategory> kakaoBizTemplateResponse = kakaoBizTalkTemplateService.getProfileCategory(trackKey, categoryCode);
        ApiResult<KakaoBizTemplateResponse<KakaoBizTemplateResponse.ProfileCategory>> response = ApiResult.<KakaoBizTemplateResponse<KakaoBizTemplateResponse.ProfileCategory>>builder()
                .code(ApiResultCode.succeed)
                .payload(kakaoBizTemplateResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
