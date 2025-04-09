package com.kep.portal.model.dto.notice.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class DeleteNoticeFileResponseDto extends ResponseDto {
    private DeleteNoticeFileResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<DeleteNoticeFileResponseDto> success(String message) {
        DeleteNoticeFileResponseDto result = new DeleteNoticeFileResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
