package com.kep.platform.service.kakao.alert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.event.PlatformEventDto;
import com.kep.core.model.dto.platform.kakao.KakaoAlertSendEvent;
import com.kep.core.model.dto.platform.kakao.KakaoBizTalkSendResponse;
import com.kep.core.model.dto.platform.kakao.bizTalk.request.TalkSendRequestDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import com.kep.core.model.dto.platform.kakao.vo.Result;
import com.kep.platform.client.PortalClient;
import com.kep.platform.client.TalkServiceClient;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 카카오 알림톡 이벤트 전송 컨슈머
 */
@Component
@Slf4j
public class SendToKakaoAlertTalkConsumer implements ChannelAwareMessageListener {

    @Resource
    private KakaoAlertTalkService kakaoAlertTalkService;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private PortalClient portalClient;
    @Resource
    private TalkServiceClient talkServiceClient;

    /**
     * 큐서버에서 받은 이벤트 처리
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("SEND TO ALERT TALK CONSUMER, BODY: {}", body);

        PlatformEventDto platformEventDto = null;
        try {
            platformEventDto = objectMapper.readValue(body, PlatformEventDto.class);
        } catch (Exception e) {
            log.error("SEND TO ALERT TALK CONSUMER, NO EVENT, FAILED: {}",
                    e.getLocalizedMessage(), e);
            return;
        }
        Assert.notNull(platformEventDto, "NO PLATFORM EVENT");

        StopWatch stopWatch = new StopWatch("SEND TO ALERT TALK CONSUMER - " + platformEventDto.getTrackKey());
        stopWatch.start();

        try {
            // 이벤트 타입 별로 처리
            switch (platformEventDto.getPlatformEventType()) {
                case MESSAGE: // 발송
                    handleMessageEvent(platformEventDto);
                    break;

                case VERIFY: // 결과 요청, TODO: 동기로 처리할지 확인 필요, 메세지별로 요청해야하면 비동기 처리
                    handleVerifyEvent(platformEventDto);
                    break;

                case CLOSE: // 결과 처리 완료, TODO: 동기로 처리할지 확인 필요, 메제시별로 요청해야하면 비동기 처리
                    handleCloseEvent(platformEventDto);
                    break;

                default:
                    log.error("SEND TO ALERT TALK CONSUMER, TRACK KEY: {}, UNKNOWN EVENT TYPE: {}, SKIP EVENT",
                            platformEventDto.getTrackKey(), platformEventDto.getPlatformEventType());
                    break;
            }
        } catch (Exception e) {
            log.error("SEND TO ALERT TALK CONSUMER, TRACK KEY: {}, FAILED: {}",
                    platformEventDto.getTrackKey(), e.getLocalizedMessage(), e);
        }

        stopWatch.stop();
        log.info("SEND TO ALERT TALK CONSUMER, END, {}ms", stopWatch.getTotalTimeMillis());
    }

    /**
     * 발송 이벤트 처리
     */
    private void handleMessageEvent(@NotNull @Valid PlatformEventDto platformEventDto) throws Exception {
        KakaoAlertSendEvent requestDto = objectMapper.readValue(platformEventDto.getPayload(), KakaoAlertSendEvent.class);
        log.debug("KAKAO ALIM TALK SEND MESSAGE = {}", requestDto);

        ResponseEntity<List<? super TalkSendResponseDto>> response = talkServiceClient.alimTalkSend(requestDto);

        if (response.hasBody()) {
            KakaoBizTalkSendResponse sendResponse =
                    new KakaoBizTalkSendResponse(objectMapper.convertValue(response.getBody(), new TypeReference<List<Result>>() {}));
            portalClient.sendMessageResponse(sendResponse);
        }
    }

    /**
     * 결과 요청 이벤트 처리
     */
    private void handleVerifyEvent(
            @NotNull @Valid PlatformEventDto platformEventDto) {
        kakaoAlertTalkService.result(platformEventDto.getTrackKey());
    }

    /**
     * 결과 처리 완료 이벤트 처리
     */
    private void handleCloseEvent(
            @NotNull @Valid PlatformEventDto platformEventDto) throws JsonProcessingException {
        List<String> uids = objectMapper.readValue(platformEventDto.getPayload(), new TypeReference<List<String>>() {
        });
        for (String uid : uids) {
            kakaoAlertTalkService.complete(platformEventDto.getTrackKey(), uid);
        }
    }
}
