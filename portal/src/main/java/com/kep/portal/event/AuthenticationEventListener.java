package com.kep.portal.event;

import com.kep.portal.model.dto.notification.NotificationDto;
import com.kep.portal.model.dto.notification.NotificationType;
import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.config.property.SystemMessageProperty;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.security.AuthMember;
import com.kep.portal.service.system.SystemEventService;
import com.kep.portal.util.BeanUtils;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class AuthenticationEventListener {

    @Resource
    private SystemMessageProperty systemMessageProperty;
    @Resource
    private SocketProperty socketProperty;
    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    @Async("eventTaskExecutor")
    public void handleAuthenticationSuccessEvent(AuthenticationSuccessEvent event) {
        if(event.getAuthentication().isAuthenticated()){

            SecurityUtils securityUtils = BeanUtils.getBean(SecurityUtils.class);
            SystemEventService systemEventService = BeanUtils.getBean(SystemEventService.class);
            AuthMember authMember = securityUtils.authMember(event.getAuthentication());
            Member fromMember = Member.builder()
                    .id(authMember.getId())
                    .branchId(authMember.getBranchId())
                    .username(authMember.getUsername())
                    .nickname(authMember.getNickname())
                    .build();

            // login team id 는 첫번째만
            systemEventService.store(fromMember, authMember.getId(), SystemEventHistoryActionType.login,"Member",null, null , authMember.getClientIp() , authMember.getUserAgent() , null , authMember.getTeamId());

            // TODO: DTO 정의 필요
            NotificationDto notificationDto = NotificationDto.builder()
                    .type(NotificationType.expired_by_duplication)
                    .payload(systemMessageProperty.getPortal().getNotification().getTitleMessage().getExpiredByDuplication())
                    .build();
            simpMessagingTemplate.convertAndSend(socketProperty.getDirectPath() + "." + authMember.getId(), notificationDto);
        }
    }

//    /**
//     * TODO: 로그인 실패시도 뭐 해야하나 ?
//     * @param event
//     */
//    @EventListener
//    public void handleAuthenticationFailureEvent(AbstractAuthenticationFailureEvent event) {
//        Exception e = event.getException();
//        Authentication authentication = event.getAuthentication();
//        log.warn("Unsuccessful authentication result: {}", authentication, e);
//    }
}