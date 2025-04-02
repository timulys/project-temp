package com.kep.portal.model.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPayload {
    NotificationDto notification;
    NotificationInfoDto notificationInfo;
}
