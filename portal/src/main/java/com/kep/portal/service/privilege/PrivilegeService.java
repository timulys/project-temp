package com.kep.portal.service.privilege;

import com.kep.core.model.dto.privilege.PrivilegeDto;
import com.kep.portal.model.entity.privilege.Privilege;
import com.kep.portal.model.entity.privilege.PrivilegeMapper;
import com.kep.portal.model.entity.privilege.RolePrivilege;
import com.kep.portal.repository.privilege.PrivilegeRepository;
import com.kep.portal.repository.privilege.RolePrivilegeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 권한, 기능 개발시 정해짐
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class PrivilegeService {

	@Resource
	private PrivilegeRepository privilegeRepository;
	@Resource
	private RolePrivilegeRepository rolePrivilegeRepository;
	@Resource
	private PrivilegeMapper privilegeMapper;

	public List<Privilege> findAll() {

		return privilegeRepository.findAll();
	}

	public List<Privilege> findAllByRoleId(@NotNull @Positive Long roleId) {

		List<RolePrivilege> rolePrivileges = rolePrivilegeRepository.findAll(Example.of(RolePrivilege.builder()
				.roleId(roleId)
				.build()));
		return rolePrivileges.stream().map(RolePrivilege::getPrivilege).collect(Collectors.toList());
	}

	public List<PrivilegeDto> getAllByRoleId(@NotNull @Positive Long roleId) {

		List<Privilege> privileges = this.findAllByRoleId(roleId);
		return privilegeMapper.map(privileges);
	}

	public Privilege findByDto(List<Privilege> privileges, PrivilegeDto privilegeDto) {

		for (Privilege privilege : privileges) {
			if (privilege.getType().equals(privilegeDto.getType())) {
				return privilege;
			}
		}

		return null;
	}
}
