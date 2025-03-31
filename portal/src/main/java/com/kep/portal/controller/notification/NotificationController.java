package com.kep.portal.controller.notification;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.guide.GuideDto;
import com.kep.core.model.dto.notification.*;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.model.dto.notification.NotificationPayload;
import com.kep.portal.model.dto.notification.response.GetNotificationListResponseDto;
import com.kep.portal.model.entity.notification.Notification;
import com.kep.portal.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Tag(name = "알림(노티) API", description = "/api/v1/notification")
@Slf4j
@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    @Resource
    private NotificationService notificationService;

    @Tag(name = "알림(노티) API")
    @Operation(summary = "알림 등록")
    @PostMapping("/{memberId}")
    public ResponseEntity<ApiResult> sendNotification(
            @Parameter(description = "사용자 아이디")
            @PathVariable(value = "memberId") Long id,
            @RequestBody NotificationPayload payload
    ) {

        NotificationDto notificationDto = NotificationDto.builder()
                .type(payload.getNotification().getType())
                .displayType(payload.getNotification().getDisplayType())
                .target(payload.getNotification().getTarget())
                .icon(payload.getNotification().getIcon())
                .payload(payload.getNotification().getPayload())
                .build();


        notificationService.store(payload.getNotificationInfo(), notificationDto);

        ApiResult response = ApiResult.builder()
                .code(ApiResultCode.succeed)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 기본 7일 알림 내역 조회
     * SB-FM-009, SB-CM-P02
     *
     * @param day
     * @return
     */
    @Tag(name = "알림(노티) API")
    @Operation(summary = "")
    @GetMapping
//    @PreAuthorize("hasAnyAuthority('READ_PORTAL', 'READ_MANAGE', 'READ_SYSTEM')")
    public ResponseEntity<ApiResult<List<NotificationDto>>> getMainNotification(
            @RequestParam(value = "day", required = false, defaultValue = "7") Long day) {
        try {
            List<NotificationDto> notificationDtoList = notificationService.getMainNotificationList(day);

            ApiResult<List<NotificationDto>> response = ApiResult.<List<NotificationDto>>builder()
                    .code(ApiResultCode.succeed)
                    .payload(notificationDtoList)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            ApiResult<List<NotificationDto>> response = ApiResult.<List<NotificationDto>>builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-CM-P02-001>>");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Tag(name = "알림(노티) API")
    @Operation(summary = "알림 목록 조회(V2)", description = "알림 목록 조회(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetNotificationListResponseDto.class)))
    @GetMapping("/v2")
    public ResponseEntity<? super GetNotificationListResponseDto> getNotificationList(
            @Parameter(description = "마지막 알림 ID(기준점)", in = ParameterIn.QUERY)
            @RequestParam(name = "last_notification_id", required = false, defaultValue = "0") Long lastNotificationId) {
        log.info("Find All Notification List Between 7days");
        ResponseEntity<? super GetNotificationListResponseDto> response = notificationService.getNotificationList(lastNotificationId);
        return response;
    }

    /**
     * 개별 읽기
     *
     * @param notificationId
     * @return
     */
    @Tag(name = "알림(노티) API")
    @Operation(summary = "")
    @PatchMapping("/read/{id}")
    @PreAuthorize("hasAnyAuthority('READ_PORTAL', 'READ_MANAGE', 'READ_SYSTEM')")
    public ResponseEntity<ApiResult> putRead(@PathVariable(value = "id") Long notificationId) {
        try {
            log.info("notificationId = {}", notificationId);
            notificationService.setReadNotificationStatus(notificationId);
            return new ResponseEntity<>(ApiResult.builder().code(ApiResultCode.succeed).build(), HttpStatus.OK);

        } catch (Exception e) {
            ApiResult response = ApiResult.builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-CM-P02-002>>");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    @Tag(name = "알림(노티) API")
    @Operation(summary = "")
    @GetMapping("/details/{memberId}")
    @PreAuthorize("hasAnyAuthority('READ_PORTAL', 'READ_MANAGE', 'READ_SYSTEM')")
    public ResponseEntity<ApiResult<Slice<NotificationDto>>> get(Pageable pageable) {

        notificationService.setReadAll();

        Slice<NotificationDto> items = notificationService.getItems(pageable);
        ApiResult response = ApiResult.builder()
                .code(ApiResultCode.succeed)
                .payload(items)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 모두 읽기
     *
     * @return
     */
    @Tag(name = "알림(노티) API")
    @Operation(summary = "")
    @PatchMapping("/read")
    @PreAuthorize("hasAnyAuthority('READ_PORTAL', 'READ_MANAGE', 'READ_SYSTEM')")
    public ResponseEntity<ApiResult> allRead() {
        try {
            notificationService.setReadAll();
            ApiResult response = ApiResult.builder()
                    .code(ApiResultCode.succeed)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResult response = ApiResult.builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-CM-P02-003>>");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

}
