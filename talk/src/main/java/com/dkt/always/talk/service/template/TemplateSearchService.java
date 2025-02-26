package com.dkt.always.talk.service.template;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.platform.kakao.bizTalk.request.TemplateSearchRequestDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TemplateResponseDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TemplateSearchResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TemplateSearchService {
    /** create methods */

    /** retrieve methods */
    ResponseEntity<? super ApiResult<List<TemplateSearchResponseDto>>> getTemplateList(TemplateSearchRequestDto requestDto, Pageable pageable);
    ResponseEntity<? super ApiResult<TemplateResponseDto>> getTemplate(Long id) throws Exception;

    /** update methods */

    /** delete methods */

}
