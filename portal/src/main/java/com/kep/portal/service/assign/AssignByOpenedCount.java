package com.kep.portal.service.assign;

import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.issue.IssueService;
import com.kep.portal.service.issue.event.EventBySystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 배정 대기 건수 제한, '배정대기 건수 제한' 자동 메세지 발송됨 ({@link EventBySystemService#sendOverAssignedAndClose})
 */
@Service
@Slf4j
public class AssignByOpenedCount implements Assignable {

    private static final String signature = "assignByOpenedCount";

    @Resource
    private IssueService issueService;
    @Resource
    private ChannelEnvService channelEnvService;
    @Resource
    private EventBySystemService eventBySystemService;
    @Resource
    private BranchService branchService;

    public AssignByOpenedCount(AssignProvider assignProvider) {
        assignProvider.addMethod(signature, this);
    }

    @Override
    public List<Member> apply(IssueAssign issueAssign, Issue issue, List<Member> members) {

        log.info("ASSIGN BY OPENED COUNT, ASSIGN: {}, ISSUE: {}, MEMBERS: {}", issueAssign, issue.getId(),
                members.stream().map(Member::getId).collect(Collectors.toList()));

        ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
        if (channelEnv == null
                || channelEnv.getAssignStandby() == null
                || channelEnv.getAssignStandby().getEnabled() == null) {
            throw new UnsupportedOperationException("AssignByOpenedCount, NO ENV");
        }

        boolean enabled = channelEnv.getAssignStandby().getEnabled();
        BranchDto branchDto = branchService.getById(issue.getBranchId());
        //issue.status open 만 체크
        if (enabled && branchDto != null &&
                WorkType.MaxCounselType.batch.equals(branchDto.getMaxCounselType())) {
            Long openedCount = issueService.countOpenedByBranchId(issue.getBranchId());
            openedCount--;
            Long assignableCount = (branchDto.getMaxCounsel() == null) ? 0L : Long.valueOf(branchDto.getMaxCounsel());
            if(openedCount >= assignableCount){
                if (issue.getAssignCount() < 2) {
                    eventBySystemService.sendOverAssignedAndClose(issue, channelEnv);
                }
                throw new UnsupportedOperationException("AssignByOpenedCount, OVER OPENED");
            }
        }

        log.info("ASSIGN BY OPENED COUNT, ISSUE: {}, MEMBERS: {}",
                issue.getId(), members.stream().map(Member::getId).collect(Collectors.toList()));

        return members;
    }
}
