package com.kep.platform.service.kakao.friend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.event.PlatformEventDto;
import com.kep.core.model.dto.platform.kakao.KakaoBizTalkSendResponse;
import com.kep.core.model.dto.platform.kakao.KakaoFriendSendEvent;
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

/**
 * 카카오 친구톡 이벤트 전송 컨슈머
 */
@Component
@Slf4j
public class SendToFriendTalkConsumer implements ChannelAwareMessageListener {
    @Resource
    private KakaoFriendTalkService kakaoFriendTalkService;
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
        log.info("SEND TO FRIEND TALK CONSUMER, BODY: {}", body);

        PlatformEventDto platformEventDto = null;
        try {
            platformEventDto = objectMapper.readValue(body, PlatformEventDto.class);
        } catch (Exception e) {
            log.error("SEND TO FRIEND TALK CONSUMER, NO EVENT, FAILED: {}",
                    e.getLocalizedMessage(), e);
            return;
        }
        Assert.notNull(platformEventDto, "NO PLATFORM EVENT");

        StopWatch stopWatch = new StopWatch("SEND TO FRIEND TALK CONSUMER - " + platformEventDto.getTrackKey());
        stopWatch.start();

        try {
            // 이벤트 타입 별로 처리
            switch (platformEventDto.getPlatformEventType()) {
                case MESSAGE: // 발송
                    handleMessageEvent(platformEventDto);
                    break;

                default:
                    log.error("SEND TO FRIEND TALK CONSUMER, TRACK KEY: {}, UNKNOWN EVENT TYPE: {}, SKIP EVENT",
                            platformEventDto.getTrackKey(), platformEventDto.getPlatformEventType());
                    break;
            }
        } catch (Exception e) {
            log.error("SEND TO FRIEND TALK CONSUMER, TRACK KEY: {}, FAILED: {}",
                    platformEventDto.getTrackKey(), e.getLocalizedMessage(), e);
        }

        stopWatch.stop();
        log.info("SEND TO FRIEND TALK CONSUMER, END, {}ms", stopWatch.getTotalTimeMillis());
    }

    /**
     * 발송 이벤트 처리
     */
    private void handleMessageEvent(@NotNull @Valid PlatformEventDto platformEventDto) throws Exception {
        KakaoFriendSendEvent requestDto = objectMapper.readValue(platformEventDto.getPayload(), KakaoFriendSendEvent.class);
        log.info("KAKAO FRIEND TALK SEND MESSAGE = {}", requestDto);

        ResponseEntity<List<? super TalkSendResponseDto>> response = talkServiceClient.friendTalkSend(requestDto);

        if (response.hasBody()) {
            KakaoBizTalkSendResponse sendResponse =
                    new KakaoBizTalkSendResponse(objectMapper.convertValue(response.getBody(), new TypeReference<List<Result>>() {}));
            portalClient.sendMessageResponse(sendResponse);
        }
    }
}
