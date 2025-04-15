package com.kep.portal.service.ai;

import com.kep.portal.model.dto.openai.response.GetChatResponseDto;
import org.springframework.http.ResponseEntity;

public interface OpenAiService {
    /** Create Methods **/


    /** Retrieve Methods **/
    ResponseEntity<? super GetChatResponseDto> findAiSummary(Long issueId);

    /** Update Methods **/


    /** Delete Methods **/
}
