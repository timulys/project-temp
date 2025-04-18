package com.kep.portal.model.dto.notice.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class PostNoticeResponseDto extends ResponseDto {
    private PostNoticeResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<PostNoticeResponseDto> success(String message) {
        PostNoticeResponseDto result = new PostNoticeResponseDto(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
