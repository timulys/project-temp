package com.kep.portal.service.assign;

import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.member.Member;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 배정
 */
public interface Assignable {

	List<Member> apply(@NotNull IssueAssign issueAssign, @NotNull Issue issue, @NotNull List<Member> members);
}
