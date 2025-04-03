package com.kep.portal.repository.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.model.dto.notification.*;
import com.kep.portal.model.entity.notification.Notification;
import com.kep.portal.model.entity.notification.NotificationMapper;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
@Slf4j
class NotificationRepositoryTest {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private NotificationService notificationService;

    @MockBean
    private MemberService memberService;

    @Resource
    private NotificationMapper notificationMapper;

    @Resource
    private SocketProperty socketProperty;
    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @BeforeEach
    void beforeEach() {

        MemberDto memberDto = MemberDto.builder()
                .id(2L)
                .nickname("유닛 테스터")
                .username("unit_tester")
                .enabled(true)
                .build();
        given(memberService.get(any(Long.class)))
                .willReturn(memberDto);
    }

    @Test
    @Disabled
    void sendEntityToDtoNotification() throws Exception{
        NotificationDisplayType displayType = NotificationDisplayType.toast;
        NotificationType type = NotificationType.done_member_transform;
        MemberDto memberDto = memberService.get(2L);

        NotificationInfoDto info = NotificationInfoDto.builder().build();

        Notification notification = Notification.builder()
                .title(notificationService.getTitleTemplate(type,info))
                .icon(NotificationIcon.member)
                .payload("변경 사유")
                .created(ZonedDateTime.now())
                .status(NotificationStatus.unread)
                .type(type)
                .displayType(displayType).build();

        NotificationDto notificationDto = notificationMapper.map(notification);
        notificationDto.setMemberId(memberDto.getId());

        log.info("notificationDto = {}", objectMapper.writeValueAsString(notificationDto));

        simpMessagingTemplate.convertAndSend(socketProperty.getDirectPath() + "."+notificationDto.getMemberId(), notificationDto);
    }

}
