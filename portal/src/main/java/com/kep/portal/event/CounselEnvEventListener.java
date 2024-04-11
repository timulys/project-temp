package com.kep.portal.event;

import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.portal.model.entity.env.CounselEnv;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.work.BranchOffDutyHours;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.system.SystemEventService;
import com.kep.portal.util.BeanUtils;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

/**
 * 상담 환경 설정
 */
@Slf4j
@Component
public class CounselEnvEventListener {

    @Async("eventTaskExecutor")
    @PostPersist
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    protected void onCreateHandler(CounselEnv counselEnv) {

        SecurityUtils securityUtils = BeanUtils.getBean(SecurityUtils.class);
        MemberService memberService = BeanUtils.getBean(MemberService.class);
        SystemEventService systemEventService = BeanUtils.getBean(SystemEventService.class);

        Member fromMember = memberService.findById(securityUtils.getMemberId());
        systemEventService.store( fromMember , securityUtils.getMemberId() ,  SystemEventHistoryActionType.system_counsel_set,"CounselEnv",null , null , null , null , "CREATE",securityUtils.getTeamId());
    }
    @Async("eventTaskExecutor")
    @PostUpdate
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    protected void onUpdateHandler(CounselEnv counselEnv) {

        SecurityUtils securityUtils = BeanUtils.getBean(SecurityUtils.class);
        MemberService memberService = BeanUtils.getBean(MemberService.class);
        SystemEventService systemEventService = BeanUtils.getBean(SystemEventService.class);

        Member fromMember = memberService.findById(securityUtils.getMemberId());
        systemEventService.store( fromMember , securityUtils.getMemberId() ,  SystemEventHistoryActionType.system_counsel_set,"CounselEnv",null , null , null , null , "UPDATE",securityUtils.getTeamId());

    }
}
