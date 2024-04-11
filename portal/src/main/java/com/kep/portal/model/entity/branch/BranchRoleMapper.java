package com.kep.portal.model.entity.branch;

import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.portal.model.entity.privilege.RoleMapper;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class BranchRoleMapper {

	@Resource
	private RoleMapper roleMapper;

	public List<BranchRole> map(Long branchId, List<RoleDto> roles) {

		List<BranchRole> branchRoles = new ArrayList<>();
		for (RoleDto roleDto : roles) {
			branchRoles.add(BranchRole.builder()
					.branchId(branchId)
					.role(roleMapper.map(roleDto))
					.build());
		}

		return branchRoles;
	}

	public List<RoleDto> map(List<BranchRole> branchRoles) {

		if (ObjectUtils.isEmpty(branchRoles)) {
			return Collections.emptyList();
		}
		return roleMapper.map(branchRoles.stream()
				.map(BranchRole::getRole)
				.collect(Collectors.toList()));
	}
}
