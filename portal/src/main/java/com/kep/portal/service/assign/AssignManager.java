package com.kep.portal.service.assign;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.service.work.OfficeHoursService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
public class AssignManager extends OfficeHoursService {

    @Resource
    private IssueRepository issueRepository;

    /**
     * 배정 받을 이슈 상태
     * @return
     */
    public List<IssueStatus> assignIssueStatus(){
        List<IssueStatus> statuses = new ArrayList<>();
        statuses.add(IssueStatus.open); //
        statuses.add(IssueStatus.ask);
        statuses.add(IssueStatus.reply);
        statuses.add(IssueStatus.assign);
        return statuses;
    }

    /**
     * 오늘 날짜 기준 (00:00 ~ 23:59)
     * 회원 IssueStatus 기준으로 방 갯수 구하기
     * @return
     */
    public Map<Long , Long> memberIssueGrouop(Long branchId , List<Member> members){
        LocalDate nowDate = LocalDate.now();

        LocalDateTime startDateTime = LocalDateTime.of(nowDate, LocalTime.of(0,0,0));
        LocalDateTime endDateTime = LocalDateTime.of(nowDate, LocalTime.of(23,59,59));

        ZonedDateTime start = ZonedDateTime.of(startDateTime , ZoneId.systemDefault());
        ZonedDateTime end = ZonedDateTime.of(endDateTime , ZoneId.systemDefault());

        //배정 해야할 상담 진행 상태
        List<IssueStatus> assignIssueStatus = this.assignIssueStatus();

        log.info("START DATETIME:{} , END DATETIME:{}" , start , end);

        //현재 진행중인 이슈 회원별 그룹
        Map<Long, Long> issueGroupMembers = issueRepository.findAllByBranchIdAndCreatedBetweenAndStatusInAndMemberIn(branchId , start , end , assignIssueStatus , members)
                        .stream()
                        .filter(q->q.getMember() != null)
                        .collect(Collectors.groupingBy(q->q.getMember().getId() , Collectors.counting()));

        log.info("ISSUE ON MEMBERS : {}" , issueGroupMembers);
        Set<Long> memberIssueGroupMemberIds = issueGroupMembers.keySet();
        for (Member member : members){
            if (!memberIssueGroupMemberIds.contains(member.getId())) {
                issueGroupMembers.put(member.getId(), 0L);
            }
        }
        return issueGroupMembers;
    }
}
