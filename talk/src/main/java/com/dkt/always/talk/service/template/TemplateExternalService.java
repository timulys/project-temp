package com.dkt.always.talk.service.template;

import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.BizTalkResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

public interface TemplateExternalService {
    /** create methods */
    ResponseEntity<? super BizTalkResponseDto> createTemplate(KakaoBizMessageTemplatePayload requestDto);

    /** retrieve methods */
    ResponseEntity<? super BizTalkResponseDto> getCategoryList();
    ResponseEntity<String> getClientId();

    /** update methods */
    ResponseEntity<? super BizTalkResponseDto> updateTemplate(KakaoBizMessageTemplatePayload requestDto);

    /** delete methods */
    ResponseEntity<? super BizTalkResponseDto> cancelTemplate(@Nullable String profileKey, @Nullable String templateCode);
}
