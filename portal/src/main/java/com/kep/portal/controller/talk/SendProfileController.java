package com.kep.portal.controller.talk;

import com.kep.core.model.dto.platform.kakao.bizTalk.response.BizTalkResponseDto;
import com.kep.portal.client.talk.SendProfileServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Biz Talk Module 우회 컨트롤러
 * By-Pass
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sendProfile")
public class SendProfileController {
    /** Autowired Components **/
    private final SendProfileServiceClient sendProfileServiceClient;


    /** Retrieve APIs **/
    @Tag(name = "프로필 키 조회")
    @Operation(summary = "프로필 키 조회(임시 우회용)")
    @GetMapping("/{sendProfileKey}")
    public ResponseEntity<? super BizTalkResponseDto> getSendProfileKey(@PathVariable("sendProfileKey") String sendProfileKey) {
        log.info("Get Sender Profile Key Information, send-profile : {}", sendProfileKey);
        ResponseEntity<? super BizTalkResponseDto> response = sendProfileServiceClient.getSendProfileKey(sendProfileKey);
        return response;
    }
}