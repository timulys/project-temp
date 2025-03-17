package com.kep.portal.controller.talk;

import com.kep.core.model.dto.platform.kakao.KakaoAlertSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import com.kep.portal.client.talk.AlimTalkServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Biz Talk Module 우회 컨트롤러
 * By-Pass
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alimtalk")
public class AlimTalkController {
    /** Autowired Components **/
    private final AlimTalkServiceClient alimTalkServiceClient;


    /** Create APIs **/
    @Tag(name = "알림톡 발송 API")
    @Operation(summary = "알림톡 발송(임시 우회용)")
    @PostMapping("/send")
    public ResponseEntity<List<? super TalkSendResponseDto>> send(@RequestBody @Valid KakaoAlertSendEvent requestDto) {
        ResponseEntity<List<? super TalkSendResponseDto>> response = alimTalkServiceClient.send(requestDto);
        return response;
    }
}
