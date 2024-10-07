package com.kep.portal.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.portal.model.entity.member.Member;
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
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;


/**
 * 상담원 model insert , update , remove
 */
@Slf4j
@Component
public class MemberEventListener {


    @Async("eventTaskExecutor")
    @PostPersist
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    protected void onCreateHandler(Member member) {

        SecurityUtils securityUtils = BeanUtils.getBean(SecurityUtils.class);
        ObjectMapper objectMapper = BeanUtils.getBean(ObjectMapper.class);
        MemberService memberService = BeanUtils.getBean(MemberService.class);
        SystemEventService systemEventService = BeanUtils.getBean(SystemEventService.class);

        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(Member.builder()
                            .username(member.getUsername())
                            .nickname(member.getNickname())
                            .branchId(member.getBranchId())
                            .enabled(member.getEnabled())
                            .managed(member.getManaged())
                            .creator(member.getCreator())
                            .created(member.getCreated())
                            .status(member.getStatus())
                            .maxCounsel(member.getMaxCounsel())
                            .outsourcing(member.getOutsourcing())
                            .setting(member.getSetting())
                    .build());
        } catch (JsonProcessingException ignored){

        }

        Member fromMember = memberService.findById(securityUtils.getMemberId());
        systemEventService.store( fromMember, member.getId(),  SystemEventHistoryActionType.member_create,"Member",null , payload , null , null , "CREATE",securityUtils.getTeamId());
    }

    @Async("eventTaskExecutor")
    @PostUpdate
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    protected void onUpdateHandler(Member member) {

    }

    @Async("eventTaskExecutor")
    @PostRemove
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    protected void toRemove(Member member) {

    }

}
