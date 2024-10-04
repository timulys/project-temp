package com.kep.portal.scheduler;

import com.kep.core.model.dto.work.WorkType;
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
public class MemberStatusSyncAssignMemberJob {

    @Resource
    private MemberService memberService;
    
    @Resource
    private OfficeHoursService officeHoursService;

    /********************************************************
     * 근무 관리 방식 '상담직원'인 경우 상담원의 온/오프라인 상태 Sync 스케줄러
     * 상담원별 Sync이기 떄문에 주기를 더 자주 실행힙니다. ( 오후 3시(15:00)부터 밤 11시(23:59)까지 10분간격으로 실행 ),
     * 다음날에 대한 온/오프라인 Sync를 위해서 자정에 한 번 더 Sync 합니다. ( 이 경우 날짜만 체크합니다. )
     * ( 임시 지정이며, 논의가 필요합니다. )
     ********************************************************/
    @Scheduled(cron = "0 0/10 15-23 * * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    @SchedulerLock(name = "MEMBER_STATUS_SYNC_ASSIGN_MEMBER"
            , lockAtLeastFor = "PT5S"
            , lockAtMostFor = "PT20S")
    public void run() throws Exception {
        List<MemberStatusSyncDto> memberAndMemberOfficeHoursList = officeHoursService.getMemberAndMemberOfficeHoursListUseBranchId(null);
        List<Member> memberList = new ArrayList<>();
        for( MemberStatusSyncDto memberAndMemberOfficeHours : memberAndMemberOfficeHoursList) {
            if(WorkType.Cases.member == memberAndMemberOfficeHours.getBranchDto().getAssign() ){
                Member member = memberService.getMemberUseMemberStatusSyncScheduler(memberAndMemberOfficeHours);
                memberList.add( member );
            }
        }
        if(memberList.size() > 0){
            memberService.memberSaveAll(memberList);
        }
    }



}
