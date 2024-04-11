package com.kep.portal.service.assign;

import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * branch -> team -> member
 * 브랜치 등록된 회원 목록
 */
@Service
@Slf4j
public class AssignByBranchMember implements Assignable {

    private static final String signature = "assignByBranchMember";

    @Resource
    private MemberService memberService;

    @Resource
    private MemberMapper memberMapper;

    public AssignByBranchMember(AssignProvider assignProvider) {
        assignProvider.addMethod(signature, this);
    }

    @Override
    public List<Member> apply(IssueAssign issueAssign, Issue issue, List<Member> members) {

        log.info("ASSIGN BY BRANCH MEMBER, ISSUE: {}, MEMBERS: {}, BRANCH: {}", issue.getId(),
                members.stream().map(Member::getId).collect(Collectors.toList()), issue.getBranchId());

        // 이전 프로세스에서 상담원 대상이 선택된 경우
        if (!members.isEmpty()) {
            return members;
        }

        members = memberService.findByTeamBelongBranch(issue.getBranchId());
        if(members.isEmpty()){
            throw new UnsupportedOperationException("AssignByBranchMember");
        }
        return members;
    }
}
