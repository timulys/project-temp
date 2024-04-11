package com.kep.portal.service.assign;

import com.kep.portal.model.entity.assign.AssignMethod;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 배정 공급
 */
@Component
@Slf4j
public class AssignProvider {

	final private Map<String, Assignable> assignableMethods = new HashMap<>();

	@Resource
	private AssignMethodService assignMethodService;

	@Resource
	private MemberMapper memberMapper;

	public AssignProvider addMethod(String signature, Assignable method) {
		assignableMethods.put(signature, method);
		return this;
	}

	/**
	 * 배정 실행
	 */
	@Nullable
	public Member applyAll(@NotNull IssueAssign issueAssign, @NotNull Issue issue , List<Member> memberList) {

		List<Assignable> assignables = getAssignableMethods();

		log.info("ASSIGN PROVIDER, APPLY, ISSUE: {}, MEMBERS: {}", issue.getId(),
				memberList.stream().map(item->memberMapper.map(item)).collect(Collectors.toList()));

		for (Assignable assignable : assignables) {
			if (assignable != null) {
				try {
					memberList = assignable.apply(issueAssign, issue, memberList);

				} catch (Exception e) {
					log.warn("ASSIGN PROVIDER, FAILED, ISSUE: {}, {}", issue.getId(), e.getLocalizedMessage());
					return null;
				}
			}
		}

		log.info("ASSIGN PROVIDER, APPLIED, ISSUE: {}, MEMBERS: {}", issue.getId(),
				memberList.stream().map(item->memberMapper.map(item)).collect(Collectors.toList()));

		return memberList.stream().findFirst().orElse(null);
	}

	/**
	 * 배졍 메소드 선택
	 */
	private List<Assignable> getAssignableMethods() {

		List<AssignMethod> assignMethods = assignMethodService.findAll();
		Assert.isTrue(!assignMethods.isEmpty(), "assign methods is empty");

		List<Assignable> assignables = new ArrayList<>();
		for (AssignMethod assignMethod : assignMethods) {
			if (assignMethod.getEnabled()) {
				assignables.add(assignableMethods.get(assignMethod.getSignature()));
			}
		}

		Assert.isTrue(!assignables.isEmpty(), "assignable methods is empty");
		return assignables;
	}
}
