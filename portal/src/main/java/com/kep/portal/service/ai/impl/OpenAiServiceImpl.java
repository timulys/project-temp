package com.kep.portal.service.ai.impl;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.client.ChatCompletionClient;
import com.kep.portal.model.dto.openai.MessageDto;
import com.kep.portal.model.dto.openai.request.PostChatRequestDto;
import com.kep.portal.model.dto.openai.response.ChatResponseDto;
import com.kep.portal.model.dto.openai.response.PostChatResponseDto;
import com.kep.portal.model.entity.issue.IssueLog;
import com.kep.portal.repository.issue.IssueLogRepository;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.service.ai.OpenAiService;
import com.kep.portal.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiServiceImpl implements OpenAiService {
    @Value("${openai.model}")
    private String model;
    @Value("${openai.api.key}")
    private String apiKey;
    private final static String ROLE_USER = "user";

    /** Autowired Components **/
    private final MessageSourceUtil messageUtil;
    private final IssueRepository issueRepository;
    private final IssueLogRepository issueLogRepository;
    private final ChatCompletionClient chatCompletionClient;

    @Override
    public ResponseEntity<? super PostChatResponseDto> findAiSummary(Long issueId) {
        boolean existedByIssueId = issueRepository.existsById(issueId);
        if(!existedByIssueId) return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.NOT_EXISTED_DATA));

        List<IssueLog> issueLogList = issueLogRepository.findAllByIssueId(issueId);
        if (issueLogList.size() == 0) return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.NOT_EXISTED_DATA));

        StringBuilder question = new StringBuilder();
        issueLogList.stream().forEach(issueLog -> question.append(issueLog.getPayload()));
        question.append("상담원의 입장에서 위 고객과의 대화 내용들 중 핵심을 뽑아서 3줄 이하로 줄바꿈해서 요약해줘.");

        MessageDto messageDto = MessageDto.builder()
                .role(ROLE_USER)
                .content(question.toString())
                .build();

        PostChatRequestDto postChatRequestDto = PostChatRequestDto.builder()
                .model(model)
                .messages(Collections.singletonList(messageDto))
                .build();

        String answer = chatCompletionClient.chatCompletions("Bearer " + apiKey, postChatRequestDto)
                .getChoices()
                .stream()
                .findFirst()
                .orElse(null)
                .getMessage()
                .getContent();

        return PostChatResponseDto.success(answer, messageUtil.success());
    }
}
