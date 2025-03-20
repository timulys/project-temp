package com.kep.portal.controller.talk;

import com.kep.core.model.dto.platform.kakao.KakaoFriendSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import com.kep.portal.client.talk.FriendTalkServiceClient;
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
@RequestMapping("/api/v1/friendtalk")
public class FriendTalkController {
    /** Autowired Components **/
    private final FriendTalkServiceClient friendTalkServiceClient;


    /** Create APIs **/
    // 친구톡 전송(단건/다건)
    @Tag(name = "친구톡 발송 API")
    @Operation(summary = "친구톡 발송(임시 우회용)")
    @PostMapping("/send")
    public ResponseEntity<List<? super TalkSendResponseDto>> send(@RequestBody @Valid KakaoFriendSendEvent requestDto) {
        ResponseEntity<List<? super TalkSendResponseDto>> response = friendTalkServiceClient.send(requestDto);
        return response;
    }
    // 이미지 업로드(와이드/일반)
    @Tag(name = "친구톡 발송 API")
    @Operation(summary = "친구톡 이미지 업로드(임시 우회용)")
    @PostMapping("/upload")
    public ResponseEntity<? super TalkSendResponseDto> upload(UploadPlatformRequestDto requestDto) throws Exception {
        ResponseEntity<? super TalkSendResponseDto> response = friendTalkServiceClient.upload(requestDto);
        return response;
    }
}
