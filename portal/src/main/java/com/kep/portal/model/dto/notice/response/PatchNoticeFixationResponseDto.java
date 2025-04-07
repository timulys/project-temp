package com.kep.portal.model.dto.notice.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PatchNoticeFixationResponseDto extends ResponseDto {
    private PatchNoticeFixationResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<PatchNoticeFixationResponseDto> success(String message) {
        PatchNoticeFixationResponseDto result = new PatchNoticeFixationResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
