package com.kep.portal.model.dto.notification.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class GetUnreadNotificationCountResponseDto extends ResponseDto {
    @Schema(description = "안읽은 알림 수")
    private final Integer unreadCount;

    private GetUnreadNotificationCountResponseDto(Integer unreadCount, String message) {
        super(ResponseCode.SUCCESS, message);
        this.unreadCount = unreadCount;
    }

    public static ResponseEntity<GetUnreadNotificationCountResponseDto> success(Integer unreadCount, String message) {
        GetUnreadNotificationCountResponseDto result =  new GetUnreadNotificationCountResponseDto(unreadCount, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
