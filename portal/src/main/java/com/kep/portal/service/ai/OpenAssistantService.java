package com.kep.portal.service.ai;

import com.kep.portal.model.dto.openai.response.GetChatResponseDto;
import org.springframework.http.ResponseEntity;

public interface OpenAssistantService {
    ResponseEntity<? super GetChatResponseDto> findSummaryAiAssistant(Long issueId);
    ResponseEntity<? super GetChatResponseDto> findAllSummaryAiAssistant(Long guestId);
}
