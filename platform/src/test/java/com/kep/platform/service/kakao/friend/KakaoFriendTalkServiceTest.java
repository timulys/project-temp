package com.kep.platform.service.kakao.friend;

import com.kep.core.model.dto.platform.BizTalkMessageType;
import com.kep.core.model.dto.platform.kakao.KakaoBizTalkSendResponse;
import com.kep.core.model.dto.platform.kakao.KakaoFriendSendEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class KakaoFriendTalkServiceTest {

    @Resource
    private KakaoFriendTalkService kakaoFriendTalkService;

    @Test
    void send() {
        List<KakaoFriendSendEvent.Message> messages = new ArrayList<>();
        messages.add(KakaoFriendSendEvent.Message.builder()
                .phoneNumber("01000000000")
                .senderNo("0211112222")
                .message("친구톡 메시지")
                .build());

        KakaoFriendSendEvent sendEvent = KakaoFriendSendEvent.builder()
                .messageType(BizTalkMessageType.FT)
                .senderKey("senderKey")
                .sendMessages(messages)
                .build();

        KakaoBizTalkSendResponse send = kakaoFriendTalkService.send(sendEvent, System.currentTimeMillis());
        log.info("send = {}", send);
    }
}