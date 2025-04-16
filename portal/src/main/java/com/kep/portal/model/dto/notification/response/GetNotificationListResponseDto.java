package com.kep.portal.model.dto.notification.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.portal.model.dto.notification.NotificationDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@ToString
public class GetNotificationListResponseDto extends ResponseDto {
    @Schema(description = "알림 목록")
    private final List<NotificationDto> notificationList;

    private GetNotificationListResponseDto(List<NotificationDto> notificationList, String message) {
        super(ResponseCode.SUCCESS, message);
        this.notificationList = notificationList;
    }

    public static ResponseEntity<GetNotificationListResponseDto> success(List<NotificationDto> notificationList, String message) {
        GetNotificationListResponseDto result = new GetNotificationListResponseDto(notificationList, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
