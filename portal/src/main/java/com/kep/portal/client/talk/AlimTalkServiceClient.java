package com.kep.portal.client.talk;

import com.kep.core.model.dto.platform.kakao.KakaoAlertSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Talk Alim-Talk 비즈니즈 우회 API Feign Client(Portal -> Talk)
 */
@FeignClient(name = "alimtalk-service", url = "${spring.cloud.discovery.client.simple.instances.talk-service[0].uri}")
public interface AlimTalkServiceClient {
    @PostMapping("/api/v2/alimtalk/send")
    ResponseEntity<List<? super TalkSendResponseDto>> send(@RequestBody @Valid KakaoAlertSendEvent requestDto);
}
