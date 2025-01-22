package com.dkt.always.talk.service.alimTalk;

import com.kep.core.model.dto.platform.kakao.KakaoAlertSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AlimTalkExternalService {
    /** Messaging Service Methods */
    ResponseEntity<List<? super TalkSendResponseDto>> sendAlimTalk(KakaoAlertSendEvent requestDto);
}