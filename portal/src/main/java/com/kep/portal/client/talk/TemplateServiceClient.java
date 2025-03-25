package com.kep.portal.client.talk;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.BizTalkResponseDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TemplateResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Talk Template 비즈니즈 우회 API Feign Client(Portal -> Talk)
 */
@FeignClient(name = "template-service", url = "${spring.cloud.discovery.client.simple.instances.talk-service[0].uri}")
public interface TemplateServiceClient {
    @PostMapping("/api/v2/template")
    ResponseEntity<? super BizTalkResponseDto> createTemplate(@RequestBody @Valid KakaoBizMessageTemplatePayload requestDto);

    @GetMapping("/api/v2/template/{id}")
    ResponseEntity<? super ApiResult<TemplateResponseDto>> getTemplate(@PathVariable("id") Long id);

    @GetMapping("/api/v2/template/category/all")
    ResponseEntity<? super BizTalkResponseDto> getCategory();

    @GetMapping("/api/v2/template/clientId")
    ResponseEntity<String> getClientId();


    @PutMapping
    ResponseEntity<? super BizTalkResponseDto> updateTemplate(@RequestBody @Valid KakaoBizMessageTemplatePayload requestDto);


    @PutMapping("/cancel/request")
    ResponseEntity<? super BizTalkResponseDto> cancelTemplate(@RequestParam(name = "profileKey") String profileKey,
                                                              @RequestParam(name = "templateCode") String templateCode);
}
