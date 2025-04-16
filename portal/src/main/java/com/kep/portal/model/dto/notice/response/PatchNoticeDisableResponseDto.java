package com.kep.portal.model.dto.notice.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class PatchNoticeDisableResponseDto extends ResponseDto {
    private PatchNoticeDisableResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<PatchNoticeDisableResponseDto> success(String message) {
        PatchNoticeDisableResponseDto result = new PatchNoticeDisableResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
