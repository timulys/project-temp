package com.kep.portal.model.dto.notice.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PatchNoticeResponseDto extends ResponseDto {
    private PatchNoticeResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<PatchNoticeResponseDto> success(String message) {
        PatchNoticeResponseDto result = new PatchNoticeResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
