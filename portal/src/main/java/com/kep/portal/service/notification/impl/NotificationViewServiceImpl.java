package com.kep.portal.service.notification.impl;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.model.dto.notification.NotificationDto;
import com.kep.portal.model.dto.notification.NotificationStatus;
import com.kep.portal.model.dto.notification.response.GetNotificationListResponseDto;
import com.kep.portal.model.dto.notification.response.GetNotificationResponseDto;
import com.kep.portal.model.dto.notification.response.GetUnreadNotificationCountResponseDto;
import com.kep.portal.model.dto.notification.response.PatchNotificationReadAllResponseDto;
import com.kep.portal.model.entity.notification.Notification;
import com.kep.portal.repository.notification.NotificationRepository;
import com.kep.portal.service.notification.NotificationViewService;
import com.kep.portal.util.MessageSourceUtil;
import com.kep.portal.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationViewServiceImpl implements NotificationViewService {
    /** Autowired Components **/
    private final SecurityUtils securityUtils;
    private final MessageSourceUtil messageUtil;
    private final NotificationRepository notificationRepository;

    /**
     * 7일 내 안읽은 알림 카운트 조회(V2)
     * @return
     */
    @Override
    public ResponseEntity<? super GetUnreadNotificationCountResponseDto> getUnreadNotificationCount() {
        ZonedDateTime end = ZonedDateTime.now();
        ZonedDateTime start = end.minusDays(7);
        start = start.truncatedTo(ChronoUnit.DAYS);
        log.info("Unread Query Start Day : {}, End Day : {}", start, end);

        Integer unreadCount = notificationRepository.countNotificationByMemberIdAndStatusAndCreatedBetween(
                securityUtils.getMemberId(), NotificationStatus.unread, start, end);

        return GetUnreadNotificationCountResponseDto.success(unreadCount, messageUtil.success());
    }

    /**
     * 7일 내 알림 기준 무한 스크롤 API(V2)
     * 25.04.02 조회 시 안읽음 데이터 우선, ID 정렬 순으로 정렬 조건 변경
     * @param lastNotificationId
     * @return
     */
    @Override
    public ResponseEntity<? super GetNotificationListResponseDto> getNotificationList(Long lastNotificationId) {
        ZonedDateTime end = ZonedDateTime.now();
        ZonedDateTime start = end.minusDays(7);
        start = start.truncatedTo(ChronoUnit.DAYS);
        log.info("Query Start Day : {}, End Day : {}", start, end);

        // 10개씩만 조회
        List<NotificationDto> notificationList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10);
        if (lastNotificationId != 0) {
            // 마지막으로 호출한 Notification ID가 있다면 무한 스크롤 실행
            notificationList = notificationRepository.findByMemberIdAndCreatedBetweenAndIdLessThanOrderByIdDesc(
                    securityUtils.getMemberId(),
                    start,
                    end,
                    lastNotificationId,
                    pageable
            ).stream().map(NotificationDto::from).collect(Collectors.toList());
        } else {
            // 없다면 최초 조회로 10개만 전달
            notificationList = notificationRepository.findByMemberIdAndCreatedBetweenOrderByIdDesc(
                    securityUtils.getMemberId(),
                    start,
                    end,
                    pageable
            ).stream().map(NotificationDto::from).collect(Collectors.toList());
        }

        if(notificationList.isEmpty()) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));

        return GetNotificationListResponseDto.success(notificationList, messageUtil.success());
    }

    /**
     * 알림 단건 읽음 처리(V2)
     * @param notificationId
     */
    @Override
    public ResponseEntity<? super GetNotificationResponseDto> patchNotificationReadStatus(Long notificationId) {
        boolean existedByNotificationId = notificationRepository.existsById(notificationId);
        if (!existedByNotificationId) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));

        Notification notification = notificationRepository.findByMemberIdAndId(securityUtils.getMemberId(), notificationId);
        notification.setStatus(NotificationStatus.read);

        return GetNotificationResponseDto.success(messageUtil.success());
    }

    /**
     * 알림 전체 읽음 처리(V2)
     */
    @Override
    public ResponseEntity<? super PatchNotificationReadAllResponseDto> patchNotificationReadAll() {
        List<Notification> notificationList =
                notificationRepository.findAllByMemberIdAndStatus(securityUtils.getMemberId(), NotificationStatus.unread);
        if (notificationList.isEmpty()) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));

        notificationList.forEach(item -> item.setStatus(NotificationStatus.read));

        return PatchNotificationReadAllResponseDto.success(messageUtil.success());
    }
}
