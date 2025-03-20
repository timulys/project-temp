package com.kep.portal.client.talk;

import com.kep.core.model.dto.platform.kakao.bizTalk.response.BizTalkResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Talk Send-Profile 비즈니즈 우회 API Feign Client(Portal -> Talk)
 */
@FeignClient(name = "sendprofile-service", url = "${spring.cloud.discovery.client.simple.instances.talk-service[0].uri}")
public interface SendProfileServiceClient {
    @GetMapping("/api/v2/sendProfile/{sendProfileKey}")
    ResponseEntity<? super BizTalkResponseDto> getSendProfileKey(@PathVariable("sendProfileKey") String sendProfileKey);
}
