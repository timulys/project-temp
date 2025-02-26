package com.dkt.always.talk.controller.friendTalk;

import com.dkt.always.talk.service.friendTalk.FriendTalkExternalService;
import com.kep.core.model.dto.platform.kakao.KakaoFriendSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
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
@RequestMapping("/api/v2/friendtalk")
@RequiredArgsConstructor
@Slf4j
public class FriendTalkController {
    /** Autowired Components **/
    private final FriendTalkExternalService friendTalkExternalService;

    /** Create APIs **/
    // 친구톡 전송(단건/다건)
    @PostMapping("/send")
    public ResponseEntity<List<? super TalkSendResponseDto>> send(@RequestBody @Valid KakaoFriendSendEvent requestDto) throws Exception {
        ResponseEntity<List<? super TalkSendResponseDto>> response = friendTalkExternalService.sendFriendTalk(requestDto);
        return response;
    }
    // 이미지 업로드(와이드/일반)
    @PostMapping("/upload")
    public ResponseEntity<? super TalkSendResponseDto> upload(UploadPlatformRequestDto requestDto) throws Exception {
        ResponseEntity<? super TalkSendResponseDto> response = friendTalkExternalService.uploadImage(requestDto);
        return response;
    }

    /** Retrieve APIs **/
    /** Update APIs **/
    /** Delete APIs **/
}
