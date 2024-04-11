package com.kep.portal.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.portal.model.dto.statistics.IssueStatisticsStatus;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.statistics.IssueStatistics;
import com.kep.portal.model.entity.work.BranchOfficeHours;
import com.kep.portal.service.issue.IssueStatisticsService;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.system.SystemEventService;
import com.kep.portal.util.BeanUtils;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.persistence.PostUpdate;

/**
 * 근무 조건 설정
 */
@Slf4j
@Component
public class BranchOfficeHoursEventListener {

    @Async("eventTaskExecutor")
    @PostUpdate
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    protected void onUpdateHandler(BranchOfficeHours branchOfficeHours) {

        SecurityUtils securityUtils = BeanUtils.getBean(SecurityUtils.class);
        MemberService memberService = BeanUtils.getBean(MemberService.class);
        SystemEventService systemEventService = BeanUtils.getBean(SystemEventService.class);

        Member fromMember = memberService.findById(securityUtils.getMemberId());
        systemEventService.store( fromMember , securityUtils.getMemberId() ,  SystemEventHistoryActionType.system_counsel_work,"BranchOfficeHours",null , null , null , null , "UPDATE" , securityUtils.getTeamId());

    }
}
