package com.kep.portal.service.assign;

import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.work.MemberOfficeHours;
import com.kep.portal.repository.assign.MemberOfficeHoursRepository;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.issue.event.EventBySystemService;
import com.kep.portal.service.work.OfficeHoursService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 회원별 근무시간 체크
 */
@Service
@Slf4j
public class AssignByMemberOfficeHours implements Assignable {

    private static final String signature = "assignByMemberOfficeHours";

    @Resource
    private OfficeHoursService officeHoursService;
    @Resource
    private MemberOfficeHoursRepository memberOfficeHoursRepository;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private BranchService branchService;
    @Resource
    private EventBySystemService eventBySystemService;

    public AssignByMemberOfficeHours(AssignProvider assignProvider) {
        assignProvider.addMethod(signature, this);
    }

    @Override
    public List<Member> apply(IssueAssign issueAssign, Issue issue, List<Member> members) {

        //근무시간 기준
        Branch branch = branchService.findById(issue.getBranchId());

        if(!WorkType.Cases.member.equals(branch.getAssign())){
            return members;
        }

        List<Long> memberIds = members.stream()
                .map(Member::getId).collect(Collectors.toList());

        log.info("ASSIGN BY MEMBER OFFICE HOURS, ISSUE: {}, MEMBERS: {}", issue.getId(), memberIds);

        List<MemberOfficeHours> memberOfficeHours = memberOfficeHoursRepository
                .findAllByMemberIdIn(memberIds);

        List<Member> memberList = new ArrayList<>();

        for (Member member : members){
            if(WorkType.OfficeHoursStatusType.on.equals(member.getStatus())){
                Optional<MemberOfficeHours> officeHoursOptional = memberOfficeHours.stream()
                        .filter(q->q.getMemberId().equals(member.getId()))
                        .findFirst();
                if (officeHoursOptional.isPresent()) {
                    MemberOfficeHours officeHours = officeHoursOptional.get();

                    //TODO: 개별 근무시간이 null 이면 무조건 근무인데 브랜치로 설정 해야할까?
                    log.info("ASSIGN BY MEMBER OFFICE HOURS, ISSUE: {}, MEMBER: {} , START : {} , END : {} , DAY OF WEEK : {}",
                            issue.getId()
                            , member.getId()
                            , officeHours.getStartCounselTime()
                            , officeHours.getEndCounselTime()
                            , officeHours.getDayOfWeek());

                    boolean isOfficeHours = officeHoursService.isOfficeHours(
                            officeHours.getStartCounselTime()
                            , officeHours.getEndCounselTime()
                            , officeHours.getDayOfWeek());

                    log.info("ASSIGN BY MEMBER OFFICE HOURS, ISSUE: {}, MEMBER: {}, IS OFFICE HOURS: {}",
                            issue.getId()
                            , member.getId()
                            , isOfficeHours);

                    if(isOfficeHours){
                        memberList.add(member);
                    }
                } else {
                    log.info("ASSIGN BY MEMBER OFFICE HOURS, NO ENV, ISSUE: {}, MEMBER: {}",
                            issue.getId(), member.getId());
                }
            } else {
                log.info("ASSIGN BY MEMBER OFFICE HOURS, OFFICE HOURS STATUS IS OFF, ISSUE: {}, MEMBER: {}",
                        issue.getId(), member.getId());
            }
        }

        log.info("ASSIGN BY MEMBER OFFICE HOURS, MEMBERS: {}, ISSUE ASSIGN: {}", memberList, issueAssign);

        if(memberList.isEmpty()){
            // 상담 요청시 (!상담원 변경 요청 && !재배정 스케줄) 상담불가 상태
            if (issueAssign.getIssueSupportYn() == null && !issueAssign.isReassigned()) {
                eventBySystemService.sendOfficeHours(issue);
            }
            throw new UnsupportedOperationException("AssignByMemberOfficeHours, EMPTY MEMBERS");
        }
        log.info("ASSIGN BY MEMBER OFFICE HOURS, ISSUE: {}, MEMBERS: {}",
                issue.getId(), memberList.stream().map(item->memberMapper.map(item)).collect(Collectors.toList()));

        return memberList;
    }

}
