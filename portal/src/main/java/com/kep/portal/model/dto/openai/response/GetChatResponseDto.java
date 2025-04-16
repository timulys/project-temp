package com.kep.portal.model.dto.openai.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class GetChatResponseDto extends ResponseDto {
    private String answer;

    private GetChatResponseDto(String answer, String message) {
        super(ResponseCode.SUCCESS, message);
        this.answer = answer;
    }

    public static ResponseEntity<GetChatResponseDto> success(String answer, String message) {
        GetChatResponseDto result = new GetChatResponseDto(answer, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
