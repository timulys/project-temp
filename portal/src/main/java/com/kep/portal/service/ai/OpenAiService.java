package com.kep.portal.service.ai;

import com.kep.portal.model.dto.openai.response.PostChatResponseDto;
import org.springframework.http.ResponseEntity;

public interface OpenAiService {
    /** Create Methods **/


    /** Retrieve Methods **/
    ResponseEntity<? super PostChatResponseDto> findAiSummary(Long issueId);

    /** Update Methods **/


    /** Delete Methods **/
}
