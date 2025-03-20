package com.kep.portal.client;

import com.kep.portal.model.dto.openai.request.PostChatRequestDto;
import com.kep.portal.model.dto.openai.response.ChatResponseDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "chat", url="https://api.openai.com/v1/")
public interface ChatCompletionClient {
    @Headers("Content-Type: application/json")
    @PostMapping("/chat/completions")
    ChatResponseDto chatCompletions(@RequestHeader("Authorization") String apiKey, PostChatRequestDto request);
}
