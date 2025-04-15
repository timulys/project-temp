package com.kep.portal.controller.assistant;

import com.kep.portal.model.dto.openai.response.GetChatResponseDto;
import com.kep.portal.service.ai.OpenAiService;
import com.kep.portal.service.ai.OpenAssistantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Open AI API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AssistantController {
    /** Autowired Components **/
    private final OpenAiService openAiService;
    private final OpenAssistantService openAssistantService;

    @Operation(summary = "상담 AI 요약")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetChatResponseDto.class)))
    @GetMapping("/{issueId}")
    public ResponseEntity<? super GetChatResponseDto> getIssueExtraSummary(@PathVariable("issueId") Long issueId) {
        log.info("AI Issue Summary, Issue ID : {}", issueId);
        ResponseEntity<? super GetChatResponseDto> response = openAiService.findAiSummary(issueId);
        log.info("AI Issue Summary, Response : {}", response);
        return response;
    }

    @Operation(summary = "상담 AI Assistant 요약")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetChatResponseDto.class)))
    @GetMapping("/assistant/{issueId}")
    public ResponseEntity<? super GetChatResponseDto> getIssueSummaryAssistant(@PathVariable("issueId") Long issueId) {
        log.info("AI Assistant Issue Summary, Issue ID : {}", issueId);
        ResponseEntity<? super GetChatResponseDto> response = openAssistantService.findSummaryAiAssistant(issueId);
        log.info("AI Assistant Issue Summary, Response : {}", response);
        return response;
    }

    @Operation(summary = "이전 상담 요약 데이터 Assistant 요약")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetChatResponseDto.class)))
    @GetMapping("/assistant-summary/{guestId}")
    public ResponseEntity<? super GetChatResponseDto> getAllIssueSummaryAssistant(@PathVariable("guestId") Long guestId) {
        log.info("AI Assistant All Issue Summary, Guest ID : {}", guestId);
        ResponseEntity<? super GetChatResponseDto> response = openAssistantService.findAllSummaryAiAssistant(guestId);
        log.info("AI Assistant All Issue Summary, Response : {}", response);
        return response;
    }
}
