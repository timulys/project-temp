package com.kep.portal.repository.notification;

import com.kep.core.model.dto.notification.NotificationStatus;
import com.kep.portal.model.entity.notification.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findTop5ByMemberIdOrderByCreatedDesc(Long memberId);

    Page<Notification> findByMemberIdAndCreatedBetween(Long memberId, ZonedDateTime start, ZonedDateTime  end, Pageable pageable);

    Slice<Notification> findAllByMemberId(Pageable pageable, Long memberId);

    Notification findAllByMemberIdAndId(Long id, Long notificationId);

    List<Notification> findAllByMemberIdAndStatus(Long id, NotificationStatus status);

    Integer countNotificationByMemberIdAndStatus(Long memberId, NotificationStatus status);
}
