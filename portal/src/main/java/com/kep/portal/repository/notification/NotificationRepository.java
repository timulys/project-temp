package com.kep.portal.repository.notification;

import com.kep.portal.model.dto.notification.NotificationStatus;
import com.kep.portal.model.entity.notification.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    Notification findByMemberIdAndId(Long id, Long notificationId);

    @Query("SELECT n FROM Notification n " +
            "WHERE n.memberId = :memberId " +
            "AND n.created BETWEEN :start AND :end " +
            "ORDER BY CASE WHEN n.status = 'UNREAD' THEN 0 ELSE 1 END, n.id DESC")
    List<Notification> findByMemberIdAndCreatedBetweenOrderByIdDesc(Long memberId, ZonedDateTime start, ZonedDateTime end, Pageable pageable);

    @Query("SELECT n FROM Notification n " +
            "WHERE n.memberId = :memberId " +
            "AND n.created BETWEEN :start AND :end " +
            "AND n.id < :lastNotificationId " +
            "ORDER BY CASE WHEN n.status = 'UNREAD' THEN 0 ELSE 1 END, n.id DESC")
    List<Notification> findByMemberIdAndCreatedBetweenAndIdLessThanOrderByIdDesc
            (Long memberId, ZonedDateTime start, ZonedDateTime end, Long lastNotificationId, Pageable pageable);

    List<Notification> findAllByMemberIdAndStatus(Long id, NotificationStatus status);

    Integer countNotificationByMemberIdAndStatusAndCreatedBetween(Long memberId, NotificationStatus status,  ZonedDateTime start, ZonedDateTime end);
}
