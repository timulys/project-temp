package com.kep.portal.service.assign;

import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.subject.IssueCategoryMember;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.subject.IssueCategoryMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 분류 배정
 */
@Component
@Slf4j
public class AssignByIssueCategoryMember implements Assignable {

	private static final String signature = "assignByIssueCategoryMember";

	@Resource
	private IssueCategoryMemberService issueCategoryMemberService;

	@Resource
	private MemberRepository memberRepository;

	@Resource
	private ChannelEnvService channelEnvService;

	public AssignByIssueCategoryMember(AssignProvider assignProvider) {

		assignProvider.addMethod(signature, this);
	}

	@Override
	public List<Member> apply(IssueAssign issueAssign, Issue issue, List<Member> members) {
		ChannelEnvDto channelEnvDto = channelEnvService.getByChannel(issue.getChannel());

		log.info("ASSIGN BY ISSUE CATEGORY, ISSUE: {}, MEMBERS: {}, CHANNEL ENV: {}", issue.getId(),
				members.stream().map(Member::getId).collect(Collectors.toList()), channelEnvDto);

		// 이전 프로세스에서 상담원 대상이 선택된 경우
		if (!members.isEmpty()) {
			return members;
		}

		// 설정값이 없는 경우
		if (ObjectUtils.isEmpty(channelEnvDto)) {
			log.info("ASSIGN BY ISSUE CATEGORY, NO ENV, SKIP, ISSUE: {}", issue.getId());
			return members;
		}

		// 분류가 없는 경우
		if (ObjectUtils.isEmpty(issue.getIssueCategory())) {
			log.info("ASSIGN BY ISSUE CATEGORY, NO CATEGORY, SKIP, ISSUE: {}", issue.getId());
			return members;
		}

		// 고객 인입시 분류 사용 안함으로 설정된 경우
		if (!SystemEnvEnum.CustomerConnection.category.equals(channelEnvDto.getCustomerConnection())) {
			log.info("ASSIGN BY ISSUE CATEGORY, NOT SET CATEGORY ENV: {}, SKIP, ISSUE: {}",
					channelEnvDto.getCustomerConnection(), issue.getId());
			return members;
		}

		//카테고리 배정
		List<Long> memberIds = issueCategoryMemberService.findAll(issue.getBranchId() , issue.getChannel().getId() , issue.getIssueCategory().getId())
				.stream().map(IssueCategoryMember::getMemberId).collect(Collectors.toList());

		log.info("ASSIGN BY ISSUE CATEGORY, ISSUE: {}, CATEGORY: {}, MEMBERS: {}",
				issue.getId(), issue.getIssueCategory().getId(), memberIds);
		return memberRepository.findByIdIn(memberIds);
	}
}
