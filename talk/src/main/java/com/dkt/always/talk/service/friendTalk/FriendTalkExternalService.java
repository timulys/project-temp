package com.dkt.always.talk.service.friendTalk;

import com.kep.core.model.dto.platform.kakao.KakaoFriendSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FriendTalkExternalService {
    ResponseEntity<List<? super TalkSendResponseDto>> sendFriendTalk(KakaoFriendSendEvent requestDto);
    ResponseEntity<? super TalkSendResponseDto> uploadImage(UploadPlatformRequestDto requestDto) throws Exception;
}
