package com.kep.portal.service.notification;

import com.kep.core.model.dto.member.MemberDto;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.config.property.SystemMessageProperty;
import com.kep.portal.model.dto.notification.*;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.notification.Notification;
import com.kep.portal.model.entity.notification.NotificationMapper;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.customer.CustomerRepository;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.notification.NotificationRepository;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.util.MessageSourceUtil;
import com.kep.portal.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.CaseUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.time.ZonedDateTime;
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
    private final BranchRepository branchRepository;
    private final MemberRepository memberRepository;
    @Autowired
    private MemberMapper memberMapper;

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

    public void storeNotice(String openType, NotificationType notificationType) {
        log.info("Create notification for register notice");

        // 공지사항을 전달할 타입에 따라 전파 대상 목록 조회
        List<MemberDto> memberDtoList = new ArrayList<>();
        if ("all".equals(openType)) {
            memberDtoList = memberRepository.findAllByEnabled(true)
                    .stream().map(member -> memberMapper.map(member)).collect(Collectors.toList());
        } else {
            memberDtoList = memberRepository.findAllByBranchIdOrderByBranchIdDesc(securityUtils.getBranchId())
                    .stream().map(member -> memberMapper.map(member)).collect(Collectors.toList());
        }

        // 공지사항 등록 알림(Notification 생성 및 전달)
        memberDtoList.stream().filter(MemberDto::getManaged).forEach(dto -> {
            NotificationDto notificationDto = NotificationDto.builder()
                    .displayType(NotificationDisplayType.toast)
                    .icon(NotificationIcon.system)
                    .target(NotificationTarget.member)
                    .type(notificationType)
                    .build();
            NotificationInfoDto notificationInfoDto = NotificationInfoDto.builder()
                    .senderId(securityUtils.getMemberId())
                    .receiverId(dto.getId())
                    .build();
            store(notificationInfoDto, notificationDto);
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
