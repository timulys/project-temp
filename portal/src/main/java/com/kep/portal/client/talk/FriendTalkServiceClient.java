package com.kep.portal.client.talk;

import com.kep.core.model.dto.platform.kakao.KakaoFriendSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Talk Friend-Talk 비즈니즈 우회 API Feign Client(Portal -> Talk)
 */
@FeignClient(name = "frientalk-service", url = "${spring.cloud.discovery.client.simple.instances.talk-service[0].uri}")
public interface FriendTalkServiceClient {
    @PostMapping("/api/v2/friendtalk/send")
    ResponseEntity<List<? super TalkSendResponseDto>> send(@RequestBody @Valid KakaoFriendSendEvent requestDto);

    @PostMapping("/api/v2/friendtalk/upload")
    ResponseEntity<? super TalkSendResponseDto> upload(UploadPlatformRequestDto requestDto);
}
