package com.dkt.always.talk.service.template;

import com.kep.core.model.dto.platform.kakao.bizTalk.response.BizTalkResponseDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface SendProfileExternalService {
    ResponseEntity<? super BizTalkResponseDto> getSendProfileKey(@NotNull String sendProfileKey);
}
