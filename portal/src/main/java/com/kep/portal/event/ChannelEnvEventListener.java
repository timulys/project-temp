package com.kep.portal.event;

import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.portal.model.entity.channel.ChannelEnv;
import com.kep.portal.model.entity.channel.ChannelEnvMapper;
import com.kep.portal.model.entity.env.CounselEnv;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.work.BranchOfficeHours;
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
 * 채널 설정 변경
 * TODO: 나중에 인터셉트 활용
 */
@Slf4j
@Component
public class ChannelEnvEventListener {

    @Async("eventTaskExecutor")
    @PostPersist
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    protected void onCreateHandler(CounselEnv counselEnv) {

        SecurityUtils securityUtils = BeanUtils.getBean(SecurityUtils.class);
        MemberService memberService = BeanUtils.getBean(MemberService.class);
        SystemEventService systemEventService = BeanUtils.getBean(SystemEventService.class);

        Member fromMember = memberService.findById(securityUtils.getMemberId());
        systemEventService.store( fromMember , securityUtils.getMemberId() ,  SystemEventHistoryActionType.system_channel,"ChannelEnv",null , null , null , null , "CREATE",securityUtils.getTeamId());
    }

    @Async("eventTaskExecutor")
    @PostUpdate
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    protected void onUpdateHandler(ChannelEnv channelEnv) {




        SecurityUtils securityUtils = BeanUtils.getBean(SecurityUtils.class);
        MemberService memberService = BeanUtils.getBean(MemberService.class);
        SystemEventService systemEventService = BeanUtils.getBean(SystemEventService.class);

        ChannelEnvMapper channelEnvMapper = BeanUtils.getBean(ChannelEnvMapper.class);

        log.info("IS CHANNEL ENV: {} ",channelEnvMapper.map(channelEnv));

        Member fromMember = memberService.findById(securityUtils.getMemberId());
        systemEventService.store( fromMember , securityUtils.getMemberId() ,  SystemEventHistoryActionType.system_channel,"ChannelEnv",null , null , null , null , "UPDATE",securityUtils.getTeamId());

    }
}
