package com.dkt.always.talk.service.alimTalk.impl;

import com.dkt.always.talk.config.property.PlatformProperty;
import com.dkt.always.talk.service.BizTalkCommonService;
import com.dkt.always.talk.service.alimTalk.AlimTalkExternalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.platform.kakao.KakaoAlertSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * AlimTalk External Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AlimTalkExternalServiceImpl extends BizTalkCommonService implements AlimTalkExternalService {
    /** AlimTalk API URL */
    private static final String ALIM_TALK_SEND_PATH = "/alimtalk/send";    // 알림톡 발송

    /** Autowired Components */
    private final PlatformProperty platformProperty;
    private final ObjectMapper objectMapper;
    private final WebClient kakaoBizTalkWebClient;

    @Override
    @CircuitBreaker(name = "default", fallbackMethod = "alimtalkExternalCallFailedMethod")
    public ResponseEntity<? super TalkSendResponseDto> sendAlimTalk(@Valid KakaoAlertSendEvent requestDto) throws Exception {
        String bodyJson = objectMapper.writeValueAsString(requestDto);
        log.info("BODY : {}", bodyJson);
        String response = kakaoBizTalkWebClient.post()
                .uri(getAlimTalkRequesetUrl(platformProperty, ALIM_TALK_SEND_PATH, "v3"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(objectMapper.writeValueAsString(requestDto)))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("send result response = {}", response);

        return ResponseEntity.ok(response);
    }

    /////////////////////////// private methods ///////////////////////////
    private ResponseEntity<ResponseDto> alimtalkExternalCallFailedMethod(Throwable throwable) {
        log.error("BizMessageCenter API Call Failed Fallback: {}", throwable.getMessage());
        return ResponseDto.alimTalkFailedMessage();
    }
}
