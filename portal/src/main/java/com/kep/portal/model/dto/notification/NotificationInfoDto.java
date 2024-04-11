package com.kep.portal.model.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 알림 정보
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationInfoDto {
    private Long senderId; // 보내는 맴버 PK
    private Long receiverId; // 받는 맴버 PK
    private Long delay; // 딜레이 시간
    private Long customerId; // 고객 PK
    private Long guestId; // 고객 PK - customerID가 없으면?
}
