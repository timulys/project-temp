package com.dkt.always.talk.service.alimTalk;

import com.kep.core.model.dto.platform.kakao.KakaoAlertSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface AlimTalkExternalService {
    /** Messaging Service Methods */
    ResponseEntity<? super TalkSendResponseDto> sendAlimTalk(@Valid KakaoAlertSendEvent requestDto) throws Exception;
}
