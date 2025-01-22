package com.kep.platform.client;

import com.kep.core.model.dto.platform.kakao.KakaoAlertSendEvent;
import com.kep.core.model.dto.platform.kakao.KakaoFriendSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Talk-Service OpenFeign Client
 */
@FeignClient(name = "talk-service", url = "${application.talk}")
public interface TalkServiceClient {
    @PostMapping("/api/v2/alimtalk/send")
    ResponseEntity<List<? super TalkSendResponseDto>> alimTalkSend(@RequestBody KakaoAlertSendEvent requestDto);

    @PostMapping("/api/v2/friendtalk/send")
    ResponseEntity<List<? super TalkSendResponseDto>> friendTalkSend(@RequestBody KakaoFriendSendEvent requestDto);

}
