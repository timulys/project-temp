package com.kep.platform.client;

import com.kep.core.model.dto.platform.kakao.KakaoAlertSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * Talk-Service OpenFeign Client
 */
@FeignClient(name = "talk-service", url = "http://localhost:8090/talk")
public interface TalkServiceClient {
    @PostMapping("/api/v3/alimtalk/send")
    ResponseEntity<? super TalkSendResponseDto> send(@RequestBody @Valid KakaoAlertSendEvent requestDto);
}
