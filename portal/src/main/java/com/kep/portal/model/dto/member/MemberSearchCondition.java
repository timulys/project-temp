package com.kep.portal.model.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "사용자(계정) 검색 조건")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSearchCondition {

	@Schema(description = "사용여부")
	private Boolean enabled;

	@Schema(description = "브랜치 아이디")
	private Long branchId;

	@Schema(description = "팀 아이디")
	private Long teamId;

	@Schema(description = "닉네임")
	private String nickname;

	@Schema(description = "전화번호")
	private String phone;

	@Schema(description = "상담원 ID")
	private String memberId;

	@Email
	@Schema(description = "메일주소")
	private String email;

	@Schema(description = "레벨 타입 목록")
	private List<String> levelType;

	// ////////////////////////////////////////////////////////////////////////
	// teamId -> members
	@Schema(description = "팀 아이디 목록")
	private Set<Long> ids;

	@Schema(description = "")
	private Boolean managed;

	// levelType -> roleIds
	@Schema(description = "레벨 타입 목록")
	private Set<Long> roleIds;

	@Schema(description = "유저 아이디")
	private String username;

}
