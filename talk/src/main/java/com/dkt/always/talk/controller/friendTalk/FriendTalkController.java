package com.dkt.always.talk.controller.friendTalk;

import com.dkt.always.talk.service.friendTalk.FriendTalkExternalService;
import com.kep.core.model.dto.platform.kakao.KakaoFriendSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TalkSendResponseDto;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

@Slf4j
@RestController
@RequestMapping("/api/v2/friendtalk")
@RequiredArgsConstructor
public class FriendTalkController {
    /** Autowired Components **/
    private final FriendTalkExternalService friendTalkExternalService;

    /** Create APIs **/
    @Tag(name = "친구톡 발송 API")
    @Operation(summary = "친구톡 전송(단건/다건)", description = "친구톡 전송(단건/다건)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = TalkSendResponseDto.class)))
    @PostMapping("/send")
    public ResponseEntity<List<? super TalkSendResponseDto>> send(@RequestBody @Valid KakaoFriendSendEvent requestDto) throws Exception {
        log.info("Friend Talk Send Event : {}", requestDto);
        ResponseEntity<List<? super TalkSendResponseDto>> response = friendTalkExternalService.sendFriendTalk(requestDto);
        log.info("Friend Talk Send Response : {}", response);
        return response;
    }
    // 이미지 업로드(와이드/일반)
    @Tag(name = "친구톡 발송 API")
    @Operation(summary = "친구톡 이미지 업로드", description = "친구톡 이미지 업로드")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = TalkSendResponseDto.class)))
    @PostMapping("/upload")
    public ResponseEntity<? super TalkSendResponseDto> upload(UploadPlatformRequestDto requestDto) throws Exception {
        log.info("Friend Talk Upload Event : {}", requestDto);
        ResponseEntity<? super TalkSendResponseDto> response = friendTalkExternalService.uploadImage(requestDto);
        log.info("Friend Talk Upload Response : {}", response);
        return response;
    }

    /** Retrieve APIs **/
    /** Update APIs **/
    /** Delete APIs **/
}
