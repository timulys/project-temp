package com.kep.portal.service.ai;

import com.kep.portal.model.dto.openai.response.PostChatResponseDto;
import org.springframework.http.ResponseEntity;

public interface OpenAssistantService {
    ResponseEntity<? super PostChatResponseDto> findAiAssistantSummary(Long issueId);
}
