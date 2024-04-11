package com.kep.portal.service.subject;

import com.kep.portal.model.entity.subject.IssueCategoryMember;
import com.kep.portal.repository.subject.IssueCategoryMemberRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class IssueCategoryMemberService {

	@Resource
	private IssueCategoryMemberRepository issueCategoryMemberRepository;
	@Resource
	private SecurityUtils securityUtils;

	public List<IssueCategoryMember> findAll(@NotNull @Positive Long branchId, @NotNull @Positive Long channelId, @NotNull @Positive Long issueCategoryId) {

		return issueCategoryMemberRepository.findAll(Example.of(IssueCategoryMember.builder()
				.branchId(branchId)
				.channelId(channelId)
				.issueCategoryId(issueCategoryId)
				.build()), Sort.by(Sort.Direction.ASC, "memberId"));
	}

	public List<Long> getMemberIds(@NotNull @Positive Long channelId, @NotNull @Positive Long issueCategoryId) {

		List<IssueCategoryMember> issueCategoryMembers = findAll(securityUtils.getBranchId(), channelId, issueCategoryId);
		return issueCategoryMembers.stream().map(IssueCategoryMember::getMemberId).collect(Collectors.toList());
	}

	public List<IssueCategoryMember> findAllByIssueCategory(@NotNull @Positive Long issueCategoryId) {

		return issueCategoryMemberRepository.findAll(Example.of(IssueCategoryMember.builder()
				.issueCategoryId(issueCategoryId)
				.build()));
	}

	public void save(@NotNull @Positive Long channelId, @NotNull @Positive Long issueCategoryId, @NotEmpty List<Long> memberIds) {

		List<IssueCategoryMember> issueCategoryMembers = findAll(securityUtils.getBranchId(), channelId, issueCategoryId);
		issueCategoryMemberRepository.deleteAll(issueCategoryMembers);
		issueCategoryMemberRepository.flush();

		issueCategoryMembers = new ArrayList<>();
		for (Long memberId : memberIds) {
			issueCategoryMembers.add(IssueCategoryMember.builder()
							.branchId(securityUtils.getBranchId())
							.channelId(channelId)
							.issueCategoryId(issueCategoryId)
							.memberId(memberId)
							.modifier(securityUtils.getMemberId())
							.modified(ZonedDateTime.now())
					.build());
		}

		issueCategoryMemberRepository.saveAll(issueCategoryMembers);
	}

	public void delete(@NotNull @Positive Long channelId, @NotNull @Positive Long issueCategoryId) {

		List<IssueCategoryMember> issueCategoryMembers = findAll(securityUtils.getBranchId(), channelId, issueCategoryId);
		issueCategoryMemberRepository.deleteAll(issueCategoryMembers);
	}
}
