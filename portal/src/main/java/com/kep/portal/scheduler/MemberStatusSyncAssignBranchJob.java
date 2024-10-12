package com.kep.portal.scheduler;

import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.model.dto.member.MemberStatusSyncDto;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.work.OfficeHoursService;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@Slf4j
@Profile(value = {"stg", "dev","kc-dev","kc-dev-cluster","local"})
@Component
public class MemberStatusSyncAssignBranchJob {

    @Resource
    private OfficeHoursService officeHoursService;

    @Resource
    private MemberService memberService;

    @Resource
    private PortalProperty portalProperty;

    /********************************************************
     * 근무 관리 방식 '시스템'인 경우 상담원의 온/오프라인 상태 Sync 스케줄러
     * 일괄 지정이기 때문에 상담원간의 상이한 부분은 없어서 자주 실행하지 않으며 (오후6시부터 밤 11시30분까지 30분간격으로 실행 ),
     * 다음날에 대한 온/오프라인 Sync를 위해서 자정에 한 번 더 Sync 합니다. ( 이 경우 날짜만 체크합니다. )
     ********************************************************/
    @Scheduled(cron = "0 0/30 18-23 * * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    @SchedulerLock(name = "MEMBER_STATUS_SYNC_ASSIGN_BRANCH"
            , lockAtLeastFor = "PT5S"
            , lockAtMostFor = "PT20S")
    public void run() throws Exception {
       log.info(">>> SCHEDULER: MEMBER STATUS SYNC ASSIGN BRANCH, START");
        List<MemberStatusSyncDto> memberAndBranchOfficeHoursList = officeHoursService.getMemberAndBranchOfficeHoursList();
        List<Member> memberList = new ArrayList<>();
        for (MemberStatusSyncDto memberAndBranchOfficeHoursDto  : memberAndBranchOfficeHoursList ){
            if( WorkType.Cases.branch == memberAndBranchOfficeHoursDto.getBranchDto().getAssign() ){
                Member member = memberService.getMemberUseMemberStatusSyncScheduler(memberAndBranchOfficeHoursDto , portalProperty.getSystemMemberId()) ;
                memberList.add( member );
            }
        }
        if(memberList.size() > 0){
            memberService.memberStatusSyncJobSaveAll(memberList);
        }
    }
}
