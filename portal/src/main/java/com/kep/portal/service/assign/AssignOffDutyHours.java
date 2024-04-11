package com.kep.portal.service.assign;

import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.work.MemberOffDutyHours;
import com.kep.portal.repository.work.MemberOffDutyHoursRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 근무예외 시간 체크
 */
@Service
@Slf4j
public class AssignOffDutyHours implements Assignable {

    private static final String signature = "assignOffDutyHours";

    @Resource
    private MemberMapper memberMapper;
    @Resource
    private MemberOffDutyHoursRepository memberOffDutyHoursRepository;

    public AssignOffDutyHours(AssignProvider assignProvider) {
        assignProvider.addMethod(signature, this);
    }

    @Override
    public List<Member> apply(IssueAssign issueAssign, Issue issue, List<Member> members) {

        log.info("ASSIGN OFF DUTY HOURS,, ISSUE: {}, MEMBERS: {}", issue.getId(),
                members.stream().map(Member::getId).collect(Collectors.toList()));

        List<Long> memberIds = members.stream()
                .map(Member::getId).collect(Collectors.toList());

        ZonedDateTime now = ZonedDateTime.now();

        List<MemberOffDutyHours> memberOffDutyHours = memberOffDutyHoursRepository
                .findAllByMemberIdInAndStartCreatedAfterAndEndCreatedBefore(memberIds , now , now);


        for (Member member : members){

            MemberOffDutyHours offDutyHours = memberOffDutyHours.stream()
                    .filter(q->q.getMemberId().equals(member.getId()))
                    .findFirst().get();

            //근무예외 시간에 걸리면 근무에서 제외
            if(offDutyHours.getEnabled() != null && !offDutyHours.getEnabled()){
                members.remove(member);
            }
        }

        log.info("OFF DUTY HOURS MEMBER LIST: {} ", members.stream()
                .map(item->memberMapper.map(item))
                .collect(Collectors.toList()));

        return members;
    }

}
