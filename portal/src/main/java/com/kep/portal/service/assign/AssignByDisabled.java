package com.kep.portal.service.assign;

import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.env.CounselEnvDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.env.CounselEnvService;
import com.kep.portal.service.issue.IssueService;
import com.kep.portal.service.issue.event.EventBySystemService;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.work.BreakTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 상담 불가 상태 제한, '상담 불가' 자동 메세지 발송됨 ({@link EventBySystemService#sendDisabledAndClose})
 * <ul>상담 불가 (배정 불가 상태)
 * <li>상담 인입 제한
 * <li>상담직원 미등록
 * <li>상담직원 모두 '상담불가'
 * </ul>
 */
@Service
@Slf4j
public class AssignByDisabled implements Assignable {

    private static final String signature = "assignByDisabled";

    @Resource
    private CounselEnvService counselEnvService;
    @Resource
    private MemberService memberService;
    @Resource
    private EventBySystemService eventBySystemService;
    @Resource
    private BranchService branchService;
    @Resource
    private IssueService issueService;
    @Resource
    private ChannelEnvService channelEnvService;

    @Resource
    private BreakTimeService breakTimeService;

    public AssignByDisabled(AssignProvider assignProvider) {
        assignProvider.addMethod(signature, this);
    }

    @Override
    public List<Member> apply(IssueAssign issueAssign, Issue issue, List<Member> members) {

        log.info("ASSIGN BY DISABLED, ISSUE: {}, MEMBERS: {}", issue.getId(),
                members.stream().map(Member::getId).collect(Collectors.toList()));

        // 상담 인입 제한
        CounselEnvDto counselEnv = counselEnvService.get(issue.getBranchId());
        if (ObjectUtils.isEmpty(counselEnv)) {
            throw new UnsupportedOperationException("AssignByDisabled, NO COUNSEL ENV");
        }

        boolean disabled = counselEnv.getRequestBlockEnabled();
        if (disabled) {
            eventBySystemService.sendDisabledAndClose(issue);
            throw new UnsupportedOperationException("AssignByDisabled, BRANCH OFF");
        }

        // breakTime 인입 제한
        boolean isBreakTime = breakTimeService.inBreakTime();
        if(isBreakTime){
            // 기존과 동일하게 로직 처리
            eventBySystemService.sendDisabledAndClose(issue); // 1. 상담 불가 메세지 발송 후 종료
            throw new UnsupportedOperationException("AssignByDisabled, BREAK TIME"); // 2. 다음 step의 상담원 배정 로직을 안타기 위해서 강제 Exception 발생
        }

        // 상담직원 미등록
        List<Member> registeredMembers = memberService.findAll(Example.of(Member.builder()
                .branchId(issue.getBranchId())
                .enabled(true).build()));
        if (registeredMembers.isEmpty()) {
            eventBySystemService.sendDisabledAndClose(issue);
            throw new UnsupportedOperationException("AssignByDisabled, NO MEMBERS");
        }

        // 상담직원 모두 '상담불가'
        boolean isStatusOff = false;
        for (Member member : registeredMembers) {
            if (WorkType.OfficeHoursStatusType.on.equals(member.getStatus())) {
                isStatusOff = true;
                break;
            }
        }
        if (!isStatusOff) {
            eventBySystemService.sendDisabledAndClose(issue);
            throw new UnsupportedOperationException("AssignByDisabled, ALL MEMBERS ARE STATUS OFF");
        }

        //최대상담 건수
        Branch branch = branchService.findById(issue.getBranchId());
        log.info("ASSIGN BY DISABLED, ISSUE: {}, MAX COUNSEL TYPE: {}, MAX COUNSEL COUNT: {}",
                issue.getId(), branch.getMaxCounselType() , branch.getMaxCounsel());
        if (WorkType.MaxCounselType.batch.equals(branch.getMaxCounselType())){
            //당일 갯수로 구한다.
            //TODO: 추후 설정으로 빠질수가 있다.
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now();

            log.info("ASSIGN BY DISABLED START :{} , END :{}", startDate, endDate);

            Long activStatusCount = issueService.countOngoing(branch.getId(), startDate, endDate);
            //내자신은 뺀다
            activStatusCount--;

            Long maxCounsel = (branch.getMaxCounsel() == null) ? 0L : Long.valueOf(branch.getMaxCounsel());

            log.info("ASSIGN BY DISABLED, ISSUE ID: {} , ASSIGN COUNT:{}, ACTIVE STATUS COUNT: {}, MAX COUNSEL COUNT: {} ",
                    issue.getId(), issue.getAssignCount() , activStatusCount , maxCounsel);
            if(maxCounsel != 0L && activStatusCount >= maxCounsel){
                if (issue.getAssignCount() < 2) {
                    ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
                    eventBySystemService.sendOverAssignedAndClose(issue,channelEnv);
                }
                throw new UnsupportedOperationException("AssignByDisabled, MAX COUNSEL TYPE(batch) , MAX COUNSEL OVER");
            }
        }
        return members;
    }
}
