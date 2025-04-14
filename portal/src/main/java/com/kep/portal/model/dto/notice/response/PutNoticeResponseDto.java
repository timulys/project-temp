package com.kep.portal.model.dto.notice.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PutNoticeResponseDto extends ResponseDto {
    private PutNoticeResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<PutNoticeResponseDto> success(String message) {
        PutNoticeResponseDto result = new PutNoticeResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
