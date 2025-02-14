package com.dkt.always.talk.service.friendTalk.impl;

import com.dkt.always.talk.config.property.PlatformProperty;
import com.dkt.always.talk.service.BizTalkCommonService;
import com.dkt.always.talk.service.friendTalk.FriendTalkExternalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.platform.kakao.KakaoFriendSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.request.TalkSendRequestDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendTalkExternalServiceImpl extends BizTalkCommonService implements FriendTalkExternalService {
    /** FriendTalk API URLs **/
    // 친구톡 발송
    private static final String FRIEND_TALK_SEND_PATH = "/send/kakao";
    // 이미지 업로드
    private static final String FRIEND_TALK_IMAGE_UPLOAD_PATH = "/upload/ft/image";
    // 와이드 이미지 업로드
    private static final String FRIEND_TALK_WIDE_IMAGE_UPLOAD_PATH = "/upload/ft/wide/image";

    /** Autowired Components **/
    private final ObjectMapper objectMapper;
    private final WebClient kakaoBizTalkWebClient;
    private final PlatformProperty platformProperty;

    /**
     * 친구톡 발송
     * @param requestDto
     * @return
     * @throws Exception
     */
    @Override
    @CircuitBreaker(name = "default", fallbackMethod = "friendTalkExternalCallFailedMethod")
    public ResponseEntity<List<? super TalkSendResponseDto>> sendFriendTalk(KakaoFriendSendEvent requestDto) {
        List<TalkSendResponseDto> responseList = requestDto.getSendMessages().stream().map(message -> {
            try {
                TalkSendResponseDto response = kakaoBizTalkWebClient.post()
                        .uri(getFriendTalkRequestUrl(platformProperty, FRIEND_TALK_SEND_PATH, "v2"))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-friendtalk"))
                        .body(BodyInserters.fromValue(objectMapper.writeValueAsString(TalkSendRequestDto.ofFriendTalk(requestDto, message))))
                        .retrieve()
                        .bodyToMono(TalkSendResponseDto.class)
                        .block();
                log.info("send result response = {}", response);
                return response;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
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
    @Override
    public ResponseEntity<? super TalkSendResponseDto> uploadImage(UploadPlatformRequestDto requestDto) throws Exception {
        log.info("SEND FRIEND TALK, IMAGE TYPE : {}", requestDto.getImageType());
        MultiValueMap<String, Object> fileMap = new LinkedMultiValueMap<>();
        fileMap.add("image", requestDto.getSourceFile().getResource());

        String path = "wide".equals(requestDto.getImageType()) ? FRIEND_TALK_WIDE_IMAGE_UPLOAD_PATH : FRIEND_TALK_IMAGE_UPLOAD_PATH;
        TalkSendResponseDto response = kakaoBizTalkWebClient.post()
                .uri(getFriendTalkRequestUrl(platformProperty, path, "v2"))
                .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-friendtalk"))
                .body(BodyInserters.fromMultipartData(fileMap))
                .retrieve()
                .bodyToMono(TalkSendResponseDto.class)
                .block();

        log.info("send image file result response = {}", response);

        return ResponseEntity.ok(response);
    }

    /////////////////////////// private methods ///////////////////////////
    private ResponseEntity<ResponseDto> friendTalkExternalCallFailedMethod(Throwable throwable) {
        log.error("BizMessageCenter Friend-Talk API Call Failed Fallback: {}", throwable.getMessage());
        return ResponseDto.friendTalkFailedMessage();
    }
}
