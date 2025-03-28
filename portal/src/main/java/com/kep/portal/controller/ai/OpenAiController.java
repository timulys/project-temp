package com.kep.portal.controller.ai;

import com.kep.portal.model.dto.customerGroup.response.GetCustomerGroupListResponseDto;
import com.kep.portal.model.dto.openai.response.PostChatResponseDto;
import com.kep.portal.service.ai.OpenAiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class OpenAiController {
    /** Autowired Components **/
    private final OpenAiService openAiService;

    @Tag(name = "이슈 상세정보 API")
    @Operation(summary = "상담 AI 요약")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = PostChatResponseDto.class)))
    @GetMapping("/{issueId}")
    public ResponseEntity<? super PostChatResponseDto> postIssueExtraSummary(@PathVariable("issueId") Long issueId) {
        log.info("AI Issue Summary, Issue ID : {}", issueId);
        ResponseEntity<? super PostChatResponseDto> response = openAiService.findAiSummary(issueId);
        return response;
    }
}
