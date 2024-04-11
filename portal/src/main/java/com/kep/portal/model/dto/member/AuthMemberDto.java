package com.kep.portal.model.dto.member;

import lombok.*;

import java.util.Map;
import java.util.Set;

/**
 * 인증(Spring Security) 유저, 프론트 노출
 * [2023.04.20/asher.shin/Dto에 setting값 및 브랜치이름 추가
 * [2023.04.60/asher.shin/Dto headquarter 및 levelId 추가
 * [2023.05.16/asher.shin/dto 외주여부 추가 ]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthMemberDto {

	private Long id;

	private String username;

	private String nickname;

	private Long branchId;

	private Set<Long> teamIds;

	private Set<String> roles;

	private Long roleId;
	
	// BNK 커스텀 유입경로 복사로 인한 처리
	private String vndrCustNo;

	private String branchName;

	private Long outsourcing;

	private boolean forbiddenWordEnabled;

	private boolean enterMessageEnabled;

	private boolean messageAutocompleteEnabled;

	private Long levelId;

	private boolean headQuarter;
}
