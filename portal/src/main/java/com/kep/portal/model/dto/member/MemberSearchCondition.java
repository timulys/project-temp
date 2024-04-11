package com.kep.portal.model.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Set;

/**
 * 유저 검색 조건
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSearchCondition {

	private Boolean enabled;

	private Long branchId;

	private Long teamId;

	private String nickname;

	private String phone;

	@Email
	private String email;

	private List<String> levelType;

	// ////////////////////////////////////////////////////////////////////////
	// teamId -> members
	private Set<Long> ids;

	private Boolean managed;

	// levelType -> roleIds
	private Set<Long> roleIds;
}
