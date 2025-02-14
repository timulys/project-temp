package com.dkt.always.talk.controller.allimTalk;

import com.dkt.always.talk.service.alimTalk.AlimTalkExternalService;
import com.kep.core.model.dto.platform.kakao.KakaoAlertSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/alimtalk")
@RequiredArgsConstructor
@Slf4j
public class AlimTalkController {
    /** Autowired Components */
    private final AlimTalkExternalService alimTalkExternalService;

    @Tag(name = "알림톡 발송 API")
    @Operation(summary = "알림톡 발송", description = "알림톡 발송")
    @PostMapping("/send")
    public ResponseEntity<List<? super TalkSendResponseDto>> send(@RequestBody @Valid KakaoAlertSendEvent requestDto) throws Exception {
        ResponseEntity<List<? super TalkSendResponseDto>> response = alimTalkExternalService.sendAlimTalk(requestDto);
        return response;
    }
}
