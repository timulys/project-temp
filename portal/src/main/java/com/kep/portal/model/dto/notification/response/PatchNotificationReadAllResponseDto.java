package com.kep.portal.model.dto.notification.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PatchNotificationReadAllResponseDto extends ResponseDto {
    private PatchNotificationReadAllResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<PatchNotificationReadAllResponseDto> success(String message) {
        PatchNotificationReadAllResponseDto result = new PatchNotificationReadAllResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
