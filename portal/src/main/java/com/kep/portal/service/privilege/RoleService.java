package com.kep.portal.service.privilege;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.minBy;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.member.MemberRole;
import com.kep.portal.repository.member.MemberRoleRepository;
import com.kep.portal.repository.privilege.RoleMenuRepository;
import com.kep.portal.repository.privilege.RolePrivilegeRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.portal.model.dto.privilege.RoleWithLevelDto;
import com.kep.portal.model.entity.branch.BranchRole;
import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.model.entity.privilege.Role;
import com.kep.portal.model.entity.privilege.RoleMapper;
import com.kep.portal.repository.branch.BranchRoleRepository;
import com.kep.portal.repository.privilege.LevelRepository;
import com.kep.portal.repository.privilege.RoleRepository;
import com.kep.portal.util.CommonUtils;
import com.kep.portal.util.SecurityUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

@Service
@Transactional
@Slf4j
public class RoleService {

	@Resource
	private RoleRepository roleRepository;
	@Resource
	private LevelService levelService;
	//@Resource
	//private LevelMenuService levelMenuService;
	@Resource
	private RoleMapper roleMapper;
	@Resource
	private SecurityUtils securityUtils;

	@Resource
	private LevelRepository levelRepository;

	@Resource
	private RoleByMenuService roleByMenuService;
	@Resource
	private RolePrivilegeService rolePrivilegeService;

	@Resource
	private BranchRoleRepository branchRoleRepository;

	@Resource
	private MemberRoleRepository memberRoleRepository;

	@Resource
	private RolePrivilegeRepository rolePrivilegeRepository;

	@Resource
	private RoleMenuRepository roleMenuRepository;

	private static final List<String> levelTypes = Arrays.asList(Level.ROLE_TYPE_OPERATOR, Level.ROLE_TYPE_MANAGER, Level.ROLE_TYPE_ADMIN);

	public List<Role> basic(Long branchId) {
		List<Level> levels = levelRepository.findAllByTypeIn(levelTypes);

		Map<Long, Optional<Role>> roles = roleRepository.findAllByLevelIn(levels).stream().collect(groupingBy(q -> q.getLevel().getId(), minBy(comparingLong(Role::getId))));

		List<BranchRole> branchRoles = new ArrayList<>();
		for (Long roleId : roles.keySet()) {
			if (roles.get(roleId).isPresent()) {
				branchRoles.add(BranchRole.builder().branchId(branchId).role(roles.get(roleId).get()).modified(ZonedDateTime.now()).modifier(securityUtils.getMemberId()).build());
			}
		}

		if (!branchRoles.isEmpty()) {
			return branchRoleRepository.saveAll(branchRoles).stream().map(BranchRole::getRole).collect(Collectors.toList());
		}
		return null;
	}

	public List<RoleDto> getAll() {

		List<Role> roles = roleRepository.findAll();
		return roleMapper.map(roles);
	}

	/**
	 * 역할 목록 (레벨 정보 포함), 마스터 레벨 제외
	 */
	public List<RoleWithLevelDto> getAllWithLevel() {

		List<Role> roles = roleRepository.findAll();

		List<Role> rolesExcludeMaster = new ArrayList<>();
		for (Role role : roles) {
			if (!Level.ROLE_TYPE_MASTER.equals(role.getLevel().getType())) {
				rolesExcludeMaster.add(role);
			}
		}

		return roleMapper.mapWithLevel(rolesExcludeMaster);
	}

	@Nullable
	public Role findOne(@NotNull Example<Role> example) {

		return roleRepository.findOne(example).orElse(null);
	}

	/**
	 * role 적용된 상담원 없으면 delete
	 * @param id
	 * @return
	 */
	public boolean destroy(@NotNull Long id){
		Role role = this.findOne(Example.of(Role.builder().id(id).build()));
		if(!ObjectUtils.isEmpty(role)){
			List<MemberRole> memberRoles = memberRoleRepository.findAllByRoleId(id);
			int count = memberRoles.size();
			if(count > 0){
				return false;
			}
			rolePrivilegeRepository.deleteAllByRoleId(id);
			roleMenuRepository.deleteAllByRoleId(id);
			roleRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public List<Role> findAllByTypeIn(@NotEmpty Collection<String> types) {

		return roleRepository.findAllByTypeIn(types);
	}

	public List<Role> findAllByIdIn(@NotNull Collection<Long> ids) {
		return roleRepository.findAllByIdIn(ids);
	}

	public List<Role> findAllByLevelTypeIn(@NotEmpty Collection<String> levelTypes) {

		List<Level> levels = levelRepository.findAllByTypeIn(levelTypes);
		return this.findAllByLevelIn(levels);
	}

	public List<Role> findAllByLevelIn(@NotEmpty Collection<Level> levels) {

		return roleRepository.findAllByLevelIn(levels);
	}



	/**
	 * 역할 생성 (역할 기본 정보 저장, 기본 권한 저장)
	 */
	public RoleDto store(@NotNull @Valid RoleDto roleDto) {

		Level level = levelService.findById(roleDto.getLevelId());
		Assert.notNull(level, "level is null");

		Role data = this.findOne(Example.of(Role.builder().name(roleDto.getName()).build()));

		//저장시 역할명 check
		if(data != null && roleDto.getId() == null){
			return null;
		}

		//수정시 역할명이 같은 pk 아니면 check
		if(data != null && !data.getId().equals(roleDto.getId())){
			return null;
		}


		// 역할 기본 정보 저장
		Role role = roleMapper.map(roleDto);
		role.setType(getType(level));
		role.setLevel(level);
		role.setModifier(securityUtils.getMemberId());
		role.setModified(ZonedDateTime.now());
		role = roleRepository.save(role);
		roleDto = roleMapper.map(role);

		// 기본 Menu 접근 권한 설정
		List<Long> menuIds = roleByMenuService.defaultSetting(role.getId(), role.getLevel().getId());
		rolePrivilegeService.defaultSetting(role.getId(), menuIds);




		return roleDto;
	}

	/**
	 * 역할 수정 (역할 기본 정보 저장)
	 */
	public RoleDto store(@NotNull @Valid RoleDto roleDto, @NotNull Long id) {
		Role role = roleRepository.findById(id).orElse(null);
		Assert.notNull(role, "role is null");

		CommonUtils.copyNotEmptyProperties(roleMapper.map(roleDto), role);
		role.setModifier(securityUtils.getMemberId());
		role.setModified(ZonedDateTime.now());
		role = roleRepository.save(role);

		return roleMapper.map(role);
	}

	/**
	 * {@link Role}.type 생성, "{@link Level}.type_(MAX_SEQUENCE + 1)"
	 */
	private String getType(@NotNull Level level) {

		List<Role> roles = roleRepository.findAllByLevel(level);

		Comparator<Role> comparatorById = Comparator.comparingLong(Role::getId);
		Role role = roles.stream().max(comparatorById).orElse(null);
		long sequence = role == null ? 1L : role.getId() + 1;

		return level.getType() + "_" + sequence;
	}

	public boolean hasLevelByRoleId(@NotNull Collection<Long> roleIds, @NotEmpty String levelType) {

		if (!roleIds.isEmpty()) {
			List<Role> roles = this.findAllByIdIn(roleIds);
			return hasLevelByRole(roles, levelType);
		}
		return false;
	}

	public boolean hasLevelByRole(@NotNull Collection<Role> roles, @NotEmpty String levelType) {

		for (Role role : roles) {
			if (levelType.equals(role.getLevel().getType())) {
				return true;
			}
		}
		return false;
	}
}