package com.kep.portal.model.dto.branch;

import com.kep.core.model.dto.privilege.RoleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * BranchDto, 브랜치/채널 관리, 브랜치 별 역할 설정
 */
@Schema(description = "브랜치/채널 관리, 브랜치 별 역할 설정")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDtoWithRole {

	@Schema(description = "브랜치 아이디")
	private Long id;

	@Schema(description = "브랜치명")
	private String name;

	@Schema(description = "")
	private Boolean headQuarters;

	@Schema(description = "역할 목록")
	private List<RoleDto> roles;
}
