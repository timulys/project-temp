package com.kep.portal.controller.notification;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.guide.GuideDto;
import com.kep.core.model.dto.notification.*;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.model.dto.notification.NotificationPayload;
import com.kep.portal.model.entity.notification.Notification;
import com.kep.portal.service.notification.NotificationService;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    @Resource
    private NotificationService notificationService;


    @PostMapping("/{memberId}")
    public ResponseEntity<ApiResult> sendNotification(@PathVariable(value = "memberId") Long id,
                                                      @RequestBody NotificationPayload payload) {

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
     * @param pageable
     * @return
     */
    @GetMapping
//    @PreAuthorize("hasAnyAuthority('READ_PORTAL', 'READ_MANAGE', 'READ_SYSTEM')")
    public ResponseEntity<ApiResult<List<NotificationDto>>> getMainNotification(
            @RequestParam(value = "day", required = false, defaultValue = "7") Long day,
            @SortDefault.SortDefaults({@SortDefault(sort = {"id"}, direction = Sort.Direction.DESC)}) Pageable pageable) {
        try {
            Page<NotificationDto> notificationDtoList = notificationService.getMainNotificationList(day, pageable);

            ApiResult<List<NotificationDto>> response = ApiResult.<List<NotificationDto>>builder()
                    .code(ApiResultCode.succeed)
                    .payload(notificationDtoList.getContent())
                    .totalPage(notificationDtoList.getTotalPages())
                    .totalElement(notificationDtoList.getTotalElements())
                    .currentPage(notificationDtoList.getNumber())
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

    /**
     * 개별 읽기
     *
     * @param notificationId
     * @return
     */
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
