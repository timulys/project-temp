package com.kep.portal.service.assign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class AssignByChannel implements Assignable {

    private static final String signature = "assignByChannel";

    @Resource
    private MemberService memberService;

    @Resource
    private ObjectMapper objectMapper;


    public AssignByChannel(AssignProvider assignProvider) {
        assignProvider.addMethod(signature, this);
    }

    @Override
    public List<Member> apply(IssueAssign issueAssign, Issue issue, List<Member> members) {
        return members;

    }

}
