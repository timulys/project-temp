package com.kep.portal.controller.notification;

import com.kep.portal.model.dto.notification.response.GetNotificationListResponseDto;
import com.kep.portal.model.dto.notification.response.GetNotificationResponseDto;
import com.kep.portal.model.dto.notification.response.GetUnreadNotificationCountResponseDto;
import com.kep.portal.model.dto.notification.response.PatchNotificationReadAllResponseDto;
import com.kep.portal.service.notification.NotificationViewService;
import com.kep.portal.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "알림(Notification) API V2", description = "/api/v1/notification")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {
    /** Autowired Components **/
    private final SecurityUtils securityUtils;
    private final NotificationViewService notificationViewService;

    /** Retrieve APIs **/
    @Operation(summary = "알림 안읽은 건수 조회(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetUnreadNotificationCountResponseDto.class)))
    @GetMapping("/unread")
    public ResponseEntity<? super GetUnreadNotificationCountResponseDto> getUnreadNotificationCount() {
        log.info("Count Unread Notification");
        ResponseEntity<? super GetUnreadNotificationCountResponseDto> response = notificationViewService.getUnreadNotificationCount();
        log.info("Get Unread, Response : {}", response.getBody());
        return response;
    }

    @Operation(summary = "알림 목록 조회(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetNotificationListResponseDto.class)))
    @GetMapping
    public ResponseEntity<? super GetNotificationListResponseDto> getNotificationList(
            @Parameter(description = "마지막 알림 ID(기준점)")
            @RequestParam(name = "last_notification_id", required = false, defaultValue = "0") Long lastNotificationId) {
        log.info("Find All Notification List Between 7days");
        ResponseEntity<? super GetNotificationListResponseDto> response = notificationViewService.getNotificationList(lastNotificationId);
        log.info("Get Notification List, Response : {}", response.getBody());
        return response;
    }

    /** Update APIs **/
    @Operation(summary = "알림 단건 읽음 처리(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetNotificationResponseDto.class)))
    @PatchMapping("/{id}")
    public ResponseEntity<? super GetNotificationResponseDto> patchNotificationReadStatus(
            @Parameter(description = "알림 ID")
            @PathVariable("id") Long id) {
        log.info("Find Notification by Notification ID : {}", id);
        ResponseEntity<? super GetNotificationResponseDto> response = notificationViewService.patchNotificationReadStatus(id);
        log.info("Get Notification, Response : {}", response.getBody());
        return response;
    }

    @Operation(summary = "알림 전체 읽음 처리(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = PatchNotificationReadAllResponseDto.class)))
    @PatchMapping("/read-all")
    public ResponseEntity<? super PatchNotificationReadAllResponseDto> patchNotificationReadAll() {
        log.info("Patch Read All Notification By Member ID : {}", securityUtils.getMemberId());
        ResponseEntity<? super PatchNotificationReadAllResponseDto> response = notificationViewService.patchNotificationReadAll();
        log.info("Patch Notification Read All, Response : {}", response.getBody());
        return response;
    }
}
