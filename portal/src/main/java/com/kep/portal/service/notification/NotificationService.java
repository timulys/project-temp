package com.kep.portal.service.notification;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.notification.NotificationDto;
import com.kep.core.model.dto.notification.NotificationStatus;
import com.kep.core.model.dto.notification.NotificationType;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.config.property.SystemMessageProperty;
import com.kep.portal.model.dto.notification.NotificationInfoDto;
import com.kep.portal.model.dto.notification.response.GetNotificationListResponseDto;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.notification.Notification;
import com.kep.portal.model.entity.notification.NotificationMapper;
import com.kep.portal.repository.customer.CustomerRepository;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.repository.notification.NotificationRepository;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.util.MessageSourceUtil;
import com.kep.portal.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.CaseUtils;
import org.slf4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(NotificationService.class);
    @Resource
    private NotificationRepository notificationRepository;
    @Resource
    private MemberService memberService;
    @Resource
    private NotificationMapper notificationMapper;

    @Resource
    private GuestRepository guestRepository;

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private SocketProperty socketProperty;
    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Resource
    private SystemMessageProperty systemMessageProperty;

    @Resource
    private SecurityUtils securityUtils;

    /** Autowired Components **/
    private final MessageSourceUtil messageUtil;

    public NotificationDto store(NotificationInfoDto info, NotificationDto requestDto) {

        return store(info, requestDto, securityUtils.getMemberId());
    }

    public NotificationDto store(NotificationInfoDto info, NotificationDto requestDto, Long memberId) {

        log.info("NOTIFICATION, NOTIFICATION INFO: {}, NOTIFICATION: {}, MEMBER: {}", info, requestDto, memberId);

        MemberDto creator = memberService.get(memberId);

        requestDto.setMemberId(info.getReceiverId());

        requestDto.setStatus(NotificationStatus.unread);
        requestDto.setTitle(getTitleTemplate(requestDto.getType(), info));
        requestDto.setCreator(creator);
        requestDto.setCreated(ZonedDateTime.now());
        if (ObjectUtils.isEmpty(requestDto.getPayload())) {
            requestDto.setPayload(requestDto.getType().getKor());
        }

        try {
            Notification save = notificationRepository.save(notificationMapper.map(requestDto));
            sendSocket(notificationMapper.map(save));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }

        return requestDto;
    }

    public List<NotificationDto> getMainNotificationList(Long day) {
        ZonedDateTime end = ZonedDateTime.now();
        ZonedDateTime start = end.minusDays(day);
        start = start.truncatedTo(ChronoUnit.DAYS);
        log.info("start = {}, end = {}", start, end);

        Pageable pageable = PageRequest.of(0, 10);
        List<Notification> items = notificationRepository.findByMemberIdAndCreatedBetweenOrderByIdDesc(securityUtils.getMemberId(), start, end, pageable);
        return notificationMapper.map(items);
    }

    /**
     * 7일 내 알림 기준 무한 스크롤 API
     * @param lastNotificationId
     * @return
     */
    // TODO : 추후 Service Interface 로 분리하여 Override 를 통해 구현할 것
    public ResponseEntity<? super GetNotificationListResponseDto> getNotificationList(Long lastNotificationId) {
        ZonedDateTime end = ZonedDateTime.now();
        ZonedDateTime start = end.minusDays(7);
        start = start.truncatedTo(ChronoUnit.DAYS);
        log.info("Query Start Day : {}, End Day : {}", start, end);

        // 10개씩만 조회
        List<NotificationDto> notificationList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10);
        if (lastNotificationId != 0) {
            // 마지막으로 호출한 Notification ID가 있다면 무한 스크롤
            notificationList = notificationRepository.findByMemberIdAndCreatedBetweenAndIdLessThanOrderByIdDesc(
                    securityUtils.getMemberId(),
                    start,
                    end,
                    lastNotificationId,
                    pageable
            ).stream().map(notification -> notificationMapper.map(notification)).collect(Collectors.toList());
        } else {
            // 없다면 최초 조회로 10개만 전달
            notificationList = notificationRepository.findByMemberIdAndCreatedBetweenOrderByIdDesc(
                    securityUtils.getMemberId(),
                    start,
                    end,
                    pageable
            ).stream().map(notification -> notificationMapper.map(notification)).collect(Collectors.toList());
        }

        if(notificationList.size() == 0) return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.NOT_EXISTED_DATA));

        return GetNotificationListResponseDto.success(notificationList, messageUtil.success());
    }

    public Integer getMainNotificationNewCount() {
        Integer unreadCount = notificationRepository.countNotificationByMemberIdAndStatus(securityUtils.getMemberId(), NotificationStatus.unread);
        return unreadCount;
    }

    public Slice<NotificationDto> getItems(@NotNull Pageable pageable) {
        Slice<Notification> items = notificationRepository.findAllByMemberId(pageable, securityUtils.getMemberId());
        List<NotificationDto> data = notificationMapper.map(items.getContent());
        return new SliceImpl<>(data, items.getPageable(), items.hasNext());
    }

    public void setReadNotificationStatus(Long notificationId) {
        Notification notification = notificationRepository.findAllByMemberIdAndId(securityUtils.getMemberId(), notificationId);
        Assert.notNull(notification, "Notification Not Found");

        notification.setStatus(NotificationStatus.read);
    }

    public void setReadAll() {
        notificationRepository.findAllByMemberIdAndStatus(securityUtils.getMemberId(), NotificationStatus.unread).forEach(item -> {
            item.setStatus(NotificationStatus.read);
        });
    }

    public String getTitleTemplate(NotificationType type, NotificationInfoDto info) {
        String titleTemplate = "Not Match Title";
        for (Field field : systemMessageProperty.getPortal().getNotification().getTitleMessage().getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String camelCase = CaseUtils.toCamelCase(type.toString(), false, new char[]{'_'});
            if (camelCase.equals(field.getName())) {
                try {
                    titleTemplate = (String) field.get(systemMessageProperty.getPortal().getNotification().getTitleMessage());
                    titleTemplate = titleReplace(titleTemplate, info);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
        return titleTemplate;
    }

    private String titleReplace(String titleTemplate, NotificationInfoDto info) {
        String customerName = null;
        //"${상담고객}" 포함되어있는지 체크
        if (titleTemplate.contains("${상담고객}")) {

            // Guest ID를 통해 Customer 정보 조회 시도
            if (info.getGuestId() != null) {
                Guest guest = guestRepository.findById(info.getGuestId()).orElse(null);
                Assert.notNull(guest, "Guest Not Found");
                log.info("DB에서 조회된 고객(게스트) ID: {}", guest.getId());

                // Guest 테이블에서 찾은 Customer ID를 이용해 Customer 테이블 조회
                if (guest.getCustomer() != null) {
                    Customer customer = customerRepository.findById(guest.getCustomer().getId()).orElse(null);
                    if (customer != null) {
                        log.info("Guest에 연결된 고객명: {}", customer.getName());
                        customerName = customer.getName();
                    } else {
                        // Customer 정보를 찾을 수 없을 때, Guest 정보를 사용하여 이름 설정
                        log.info("DB에서 조회된 고객(게스트) 이름: {}", guest.getId());
                        customerName = "고객" + guest.getId();
                    }
                } else if (guest.getId() != null) {
                    // Synk 되지 않은 Guest는 Guest Name으로 상담고객 치환(KICA-542)
                    log.info("DB에서 조회된 고객(게스트) 이름: {}", guest.getName());
                    customerName = guest.getName();
                }
            }

            // Guest 정보가 없거나 Customer ID가 있을 경우, 해당 ID로 고객명 조회
            if (customerName == null && info.getCustomerId() != null) {
                Customer customer = customerRepository.findById(info.getCustomerId()).orElse(null);
                Assert.notNull(customer, "Customer Not Found");
                log.info("DB에서 조회된 고객명: {}", customer.getName());
                customerName = customer.getName();
            }

            // 찾은 고객명으로 titleTemplate 내용 교체
            if (customerName != null) {
                titleTemplate = titleTemplate.replace("${상담고객}", customerName);
            }
        }
        if (titleTemplate.contains("${요청 상담직원}")) {
            if (info.getSenderId() != null) {
                MemberDto memberDto = memberService.get(info.getSenderId());

                titleTemplate = titleTemplate.replace("${요청 상담직원}", memberDto.getNickname());
            }
        }
        if (titleTemplate.contains("${받는 상담직원}")) {
            if (info.getSenderId() != null) {
                MemberDto memberDto = memberService.get(info.getAssigneeId());

                titleTemplate = titleTemplate.replace("${받는 상담직원}", memberDto.getNickname());
            }
        }

        if (titleTemplate.contains("${m}")) {
            if (info.getDelay() != null) {
                titleTemplate = titleTemplate.replace("${m}", info.getDelay().toString());
            }
        }

        return titleTemplate;
    }


    public void sendSocket(NotificationDto dto) {
        switch (dto.getTarget()) {
            case all:
                simpMessagingTemplate.convertAndSend(socketProperty.getGeneralPath(), dto);
                break;
            case branch:
                simpMessagingTemplate.convertAndSend(socketProperty.getBranchPath() + "." + dto.getCreator().getBranch().getId(), dto);
                break;
            case member:
                simpMessagingTemplate.convertAndSend(socketProperty.getDirectPath() + "." + dto.getMemberId(), dto);
                break;
            case manager:
                simpMessagingTemplate.convertAndSend(socketProperty.getManagerPath() + "." + dto.getCreator().getBranch().getId(), dto);
                break;
            case admin:
                simpMessagingTemplate.convertAndSend(socketProperty.getAdminPath() + "." + dto.getCreator().getBranch().getId(), dto);
                break;
            case manager_admin:
                simpMessagingTemplate.convertAndSend(socketProperty.getManagerAdminPath() + "." + dto.getCreator().getBranch().getId(), dto);
                break;
        }
    }
}
