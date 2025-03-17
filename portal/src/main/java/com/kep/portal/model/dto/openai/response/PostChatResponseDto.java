package com.kep.portal.model.dto.openai.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PostChatResponseDto extends ResponseDto {
    private String answer;

    private PostChatResponseDto(String answer, String message) {
        super(ResponseCode.SUCCESS, message);
        this.answer = answer;
    }

    public static ResponseEntity<PostChatResponseDto> success(String answer, String message) {
        PostChatResponseDto result = new PostChatResponseDto(answer, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
