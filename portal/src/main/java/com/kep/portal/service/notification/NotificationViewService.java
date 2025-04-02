package com.kep.portal.service.notification;

import com.kep.portal.model.dto.notification.response.GetNotificationListResponseDto;
import com.kep.portal.model.dto.notification.response.GetNotificationResponseDto;
import com.kep.portal.model.dto.notification.response.GetUnreadNotificationCountResponseDto;
import com.kep.portal.model.dto.notification.response.PatchNotificationReadAllResponseDto;
import org.springframework.http.ResponseEntity;

public interface NotificationViewService {
    /** Create Methods **/
    /** Retrieve Methods **/
    ResponseEntity<? super GetUnreadNotificationCountResponseDto> getUnreadNotificationCount();
    ResponseEntity<? super GetNotificationListResponseDto> getNotificationList(Long lastNotificationId);
    /** Update Methods **/
    ResponseEntity<? super GetNotificationResponseDto> patchNotificationReadStatus(Long notificationId);
    ResponseEntity<? super PatchNotificationReadAllResponseDto> patchNotificationReadAll();
    /** Delete Methods **/

}
