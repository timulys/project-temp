package com.dkt.always.talk.controller.template;

import com.dkt.always.talk.service.template.SendProfileExternalService;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.BizTalkResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/sendProfile")
@RequiredArgsConstructor
@Slf4j
public class SendProfileController {
    // Autowired Components
    private final SendProfileExternalService sendProfileExternalService;

    @Tag(name = "프로필 키 조회")
    @Operation(summary = "프로필 키 조회")
    @GetMapping("/{sendProfileKey}")
    public ResponseEntity<? super BizTalkResponseDto> getSendProfileKey(
            @NotNull @PathVariable(value = "sendProfileKey") String sendProfileKey) {
        ResponseEntity<? super BizTalkResponseDto> response = sendProfileExternalService.getSendProfileKey(sendProfileKey);
        return response;
    }
}
