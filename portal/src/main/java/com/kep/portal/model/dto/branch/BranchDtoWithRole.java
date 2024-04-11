package com.kep.portal.model.dto.branch;

import com.kep.core.model.dto.privilege.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * BranchDto, 브랜치/채널 관리, 브랜치 별 역할 설정
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDtoWithRole {

	private Long id;

	private String name;

	private Boolean headQuarters;

	private List<RoleDto> roles;
}
