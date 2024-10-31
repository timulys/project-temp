package com.kep.portal.repository.member;

import com.kep.core.model.dto.member.MemberDto;
import com.kep.portal.model.dto.member.MemberAssignDto;
import com.kep.portal.model.dto.member.MemberSearchCondition;
import com.kep.portal.model.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface MemberSearchRepository {

	Page<Member> search(@NotNull MemberSearchCondition condition, @NotNull Pageable pageable);

	List<MemberDto> findMemberUseTeamId(Long teamId);

	Page<MemberAssignDto> searchAssignableMember(@NotNull MemberSearchCondition condition, @NotNull Pageable pageable);
}
