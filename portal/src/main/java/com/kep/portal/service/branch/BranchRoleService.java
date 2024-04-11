package com.kep.portal.service.branch;

import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.portal.model.dto.branch.BranchDtoWithRole;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchRole;
import com.kep.portal.model.entity.branch.BranchRoleMapper;
import com.kep.portal.model.entity.privilege.Role;
import com.kep.portal.model.entity.privilege.RoleMapper;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.branch.BranchRoleRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link Branch}, {@link Role} 매칭
 */
@Service
@Transactional
@Slf4j
public class BranchRoleService {

	@Resource
	private BranchRoleRepository branchRoleRepository;
	@Resource
	private BranchRepository branchRepository;
	@Resource
	private BranchRoleMapper branchRoleMapper;
	@Resource
	private RoleMapper roleMapper;
	@Resource
	private SecurityUtils securityUtils;

	public List<BranchDtoWithRole> getAllWithRole() {

		List<Branch> branches = branchRepository.findAll(Example.of(Branch.builder()
				.enabled(true).build()));

		List<BranchDtoWithRole> branchDtoWithRoles = new ArrayList<>();
		for (Branch branch : branches) {
			List<BranchRole> branchRoles = this.getAllByBranchId(branch.getId());
			List<RoleDto> roles = branchRoleMapper.map(branchRoles);

			branchDtoWithRoles.add(BranchDtoWithRole.builder()
					.id(branch.getId())
					.name(branch.getName())
					.headQuarters(branch.getHeadQuarters())
					.roles(roles)
					.build());
		}

		return branchDtoWithRoles;
	}

	public BranchDtoWithRole getWithRole(@NotNull @Positive Long branchId) {

		Branch branch = branchRepository.findById(branchId).orElse(null);
		Assert.notNull(branch, "BRANCH NOT FOUND");

		List<BranchRole> branchRoles = this.getAllByBranchId(branch.getId());
		List<RoleDto> roles = branchRoleMapper.map(branchRoles);

		return BranchDtoWithRole.builder()
				.id(branch.getId())
				.name(branch.getName())
				.headQuarters(branch.getHeadQuarters())
				.roles(roles)
				.build();
	}

	public List<BranchRole> getAllByBranchId(@NotNull @Positive Long branchId) {

		return branchRoleRepository.findAll(Example.of(BranchRole.builder()
				.branchId(branchId)
				.build()));
	}

	public BranchDtoWithRole store(@NotNull @Valid BranchDtoWithRole branchDtoWithRole) {

		List<BranchRole> branchRoles = this.save(branchDtoWithRole.getId(), branchDtoWithRole.getRoles());
		branchDtoWithRole.setRoles(branchRoleMapper.map(branchRoles));
		return branchDtoWithRole;
	}

	public List<BranchRole> save(@NotNull @Positive Long branchId, @NotNull List<RoleDto> roles) {

		// 전체 삭제
//		branchRoleRepository.deleteAllByBranchId(branchId);

		branchRoleRepository.deleteAllInBatchByBranchId(branchId);
		branchRoleRepository.flush();
		// 전체 저장
		Set<RoleDto> roleDtoSet = new HashSet<>(roles);
		List<BranchRole> branchRoles = new ArrayList<>();
		for (RoleDto role : roleDtoSet) {
			branchRoles.add(BranchRole.builder()
					.branchId(branchId)
					.role(roleMapper.map(role))
					.modifier(securityUtils.getMemberId())
					.modified(ZonedDateTime.now())
					.build());
		}

		if (!branchRoles.isEmpty()) {
			branchRoles = branchRoleRepository.saveAll(branchRoles);
		}

		return branchRoles;
	}
}
