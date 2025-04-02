package com.kep.portal.model.dto.notification.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetNotificationResponseDto extends ResponseDto {
    private GetNotificationResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<GetNotificationResponseDto> success(String message) {
        GetNotificationResponseDto result = new GetNotificationResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
