package com.dkt.always.talk.service.alimTalk.impl;

import com.dkt.always.talk.config.property.PlatformProperty;
import com.dkt.always.talk.service.BizTalkCommonService;
import com.dkt.always.talk.service.alimTalk.AlimTalkExternalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.platform.kakao.KakaoAlertSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.request.TalkSendRequestDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AlimTalk External Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AlimTalkExternalServiceImpl extends BizTalkCommonService implements AlimTalkExternalService {
    /** AlimTalk API URLs */
    // 알림톡 발송
    private static final String ALIM_TALK_SEND_PATH = "/send/kakao";

    /** Autowired Components */
    private final ObjectMapper objectMapper;
    private final WebClient kakaoBizTalkWebClient;
    private final PlatformProperty platformProperty;

    /**
     * 알림톡 발송
     * @param requestDto
     * @return
     * @throws Exception
     */
    @Override
    @CircuitBreaker(name = "default", fallbackMethod = "alimTalkExternalCallFailedMethod")
    public ResponseEntity<List<? super TalkSendResponseDto>> sendAlimTalk(KakaoAlertSendEvent requestDto) {
        List<TalkSendResponseDto> responseList = requestDto.getSendMessages().stream().map(message -> {
            try {
                TalkSendResponseDto response = kakaoBizTalkWebClient.post()
                        .uri(getAlimTalkRequestUrl(platformProperty, ALIM_TALK_SEND_PATH, "v2"))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("kakao-alimtalk"))
                        .body(BodyInserters.fromValue(objectMapper.writeValueAsString(TalkSendRequestDto.ofAlimTalk(requestDto, message))))
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

    /////////////////////////// private methods ///////////////////////////
    private ResponseEntity<ResponseDto> alimTalkExternalCallFailedMethod(Throwable throwable) {
        log.error("BizMessageCenter Alim-Talk API Call Failed Fallback: {}", throwable.getMessage());
        return ResponseDto.alimTalkFailedMessage();
    }
}
