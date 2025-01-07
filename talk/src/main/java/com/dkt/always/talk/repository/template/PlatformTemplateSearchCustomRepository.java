package com.dkt.always.talk.repository.template;

import com.dkt.always.talk.entity.template.PlatformTemplate;
import com.kep.core.model.dto.platform.kakao.bizTalk.request.TemplateSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlatformTemplateSearchCustomRepository {
    Page<PlatformTemplate> search(TemplateSearchRequestDto requestDto, Pageable pageable);
}
