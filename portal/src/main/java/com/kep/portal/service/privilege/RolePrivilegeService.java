package com.kep.portal.service.privilege;

import com.kep.core.model.dto.privilege.PrivilegeDto;
import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.portal.model.entity.privilege.*;
import com.kep.portal.repository.privilege.RolePrivilegeRepository;
import com.kep.portal.repository.privilege.RoleRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link Role}, {@link Privilege} 매칭
 */
@Service
@Transactional
@Slf4j
public class RolePrivilegeService {

    @Resource
    private RolePrivilegeRepository rolePrivilegeRepository;
    @Resource
    private LevelPrivilegeService levelPrivilegeService;
    @Resource
    private PrivilegeService privilegeService;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private PrivilegeMapper privilegeMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private MenuPrivilegeService menuPrivilegeService;
    @Resource
    private SecurityUtils securityUtils;

    /**
     * 역할별 권한 저장
     */
    public RoleDto store(@NotNull @Valid RoleDto roleDto, @NotNull Long roleId) {

        Role role = roleRepository.findById(roleId).orElse(null);
        Assert.notNull(role, "role is null");

        // 기본 권한 (Privileges of Level)
        List<LevelPrivilege> levelPrivileges = levelPrivilegeService.findByLevelId(role.getLevel().getId());
        Set<PrivilegeDto> privileges = levelPrivileges.stream().map(o -> privilegeMapper.map(o.getPrivilege())).collect(Collectors.toSet());

        // 요청된 권한 (Privileges from RoleDto)
        if (!ObjectUtils.isEmpty(roleDto.getPrivileges())) {
            privileges.addAll(roleDto.getPrivileges());
        }

        roleDto = roleMapper.map(role);
        roleDto.setPrivileges(this.store(role.getId(), privileges));
        return roleDto;
    }

    /**
     * 역할별 권한 저장, 전체 삭제 후 전체 생성
     */
    public List<PrivilegeDto> store(@NotNull @Positive Long roleId, @NotEmpty Set<PrivilegeDto> privilegeDtos) {

        List<Privilege> privileges = privilegeService.findAll();

        // List<PrivilegeDto> -> List<RolePrivilege>
        List<RolePrivilege> rolePrivileges = new ArrayList<>();
        for (PrivilegeDto privilegeDto : privilegeDtos) {
            Privilege privilege = privilegeService.findByDto(privileges, privilegeDto);
            Assert.notNull(privilege, "privilege is null");
            RolePrivilege rolePrivilege = RolePrivilege.builder()
                    .roleId(roleId)
                    .privilege(privilege)
                    .creator(securityUtils.getMemberId())
                    .created(ZonedDateTime.now())
                    .build();
            rolePrivileges.add(rolePrivilege);
        }

        // 전체 삭제
        rolePrivilegeRepository.deleteAllByIdInBatch(rolePrivilegeRepository.findAllByRoleId(roleId)
                .stream().map(RolePrivilege::getId).collect(Collectors.toList()));
        rolePrivilegeRepository.flush();
        // 전체 생성
        rolePrivileges = rolePrivilegeRepository.saveAll(rolePrivileges);

        return this.getPrivileges(rolePrivileges);
    }

    /**
     * List<RolePrivilege> -> List<PrivilegeDto>
     */
    public List<PrivilegeDto> getPrivileges(@NotNull List<RolePrivilege> rolePrivileges) {

        List<Privilege> privilegeEntities = rolePrivileges.stream().map(RolePrivilege::getPrivilege).collect(Collectors.toList());
        return privilegeMapper.map(privilegeEntities);
    }

    public void defaultSetting(Long roleId, List<Long> menuIds) {
        List<RolePrivilege> rolePrivileges = new ArrayList<>();

        List<Privilege> privileges = menuPrivilegeService.findAllByMenuIdIn(menuIds).stream().map(MenuPrivilege::getPrivilege).collect(Collectors.toList());

        for (Privilege privilege : privileges) {
            rolePrivileges.add(RolePrivilege.builder()
                    .privilege(privilege)
                    .roleId(roleId)
                    .creator(securityUtils.getMemberId())
                    .build());
        }
        rolePrivilegeRepository.saveAll(rolePrivileges);
    }
}
