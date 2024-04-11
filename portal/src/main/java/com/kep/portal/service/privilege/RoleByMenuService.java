package com.kep.portal.service.privilege;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.privilege.PrivilegeDto;
import com.kep.portal.model.dto.privilege.RoleByMenuDto;
import com.kep.portal.model.entity.privilege.LevelMenu;
import com.kep.portal.model.entity.privilege.MenuPrivilege;
import com.kep.portal.model.entity.privilege.PrivilegeMapper;
import com.kep.portal.model.entity.site.Menu;
import com.kep.portal.model.entity.privilege.RoleMenu;
import com.kep.portal.repository.site.MenuRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 시스템 설정 > 계정 관리 > 권한 관리
 */
@Service
@Transactional
@Slf4j
public class RoleByMenuService {

    @Resource
    private MenuRepository menuRepository;
    @Resource
    private RoleMenuService roleMenuService;
    @Resource
    private MenuPrivilegeService menuPrivilegeService;
    @Resource
    private RolePrivilegeService rolePrivilegeService;
    @Resource
    private PrivilegeMapper privilegeMapper;
    @Resource
    private SecurityUtils securityUtils;
    @Resource
    private LevelMenuService levelMenuService;
    @Resource
    private ObjectMapper objectMapper;

    public List<RoleByMenuDto> getAll() {

        List<Menu> menus = menuRepository.findAll(Example.of(Menu.builder()
                .enabled(true)
                .roleEnabled(true)
                .build()), Sort.by(Sort.Direction.ASC, "sort"));
        List<RoleByMenuDto> roleByMenuDtos = new ArrayList<>();

        for (Menu menu : menus) {
            List<RoleMenu> roleMenus = roleMenuService.findAllByMenuId(menu.getId());
            Set<Long> roleIds = roleMenus.stream().map(RoleMenu::getRoleId).collect(Collectors.toSet());

            RoleByMenuDto roleByMenuDto = RoleByMenuDto.builder()
                    .menuId(menu.getId())
                    .menuName1(menu.getName1())
                    .menuName2(menu.getName2())
                    .menuName3(menu.getName3())
                    .menuName4(menu.getName4())
                    .roleIds(roleIds)
                    .build();
            roleByMenuDtos.add(roleByMenuDto);
            if (!ObjectUtils.isEmpty(menu.getDisabledLevels())) {
                roleByMenuDto.setDisabledLevels(Arrays.asList(menu.getDisabledLevels().split(","))
                        .stream().map(String::trim).collect(Collectors.toSet()));
            }
        }

        return roleByMenuDtos;
    }

    public void save(@NotEmpty List<RoleByMenuDto> roleByMenuDtos) {

        // 역할-메뉴 매칭 저장
        // 전체 삭제
        roleMenuService.deleteAll();
        // 전체 생성
        List<RoleMenu> roleMenus = new ArrayList<>();
        for (RoleByMenuDto roleByMenuDto : roleByMenuDtos) {
            if (!ObjectUtils.isEmpty(roleByMenuDto.getRoleIds())) {
                for (Long roleId : roleByMenuDto.getRoleIds()) {
                    roleMenus.add(RoleMenu.builder()
                            .roleId(roleId)
                            .menuId(roleByMenuDto.getMenuId())
                            .modifier(securityUtils.getMemberId())
                            .modified(ZonedDateTime.now())
                            .build());
                }
            }
        }
        roleMenus = roleMenuService.saveAll(roleMenus);

        // 메뉴에 매칭된 권한을 사용해, 역할-권한 저장
        Map<Long, List<RoleMenu>> roleIdMap = roleMenus.stream().collect(Collectors.groupingBy(RoleMenu::getRoleId));
        for (Long roleId : roleIdMap.keySet()) {
            try {
                log.debug("{}, {}", roleId, objectMapper.writeValueAsString(roleIdMap.get(roleId)));
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
            }
            Set<PrivilegeDto> privileges = new HashSet<>();
            for (RoleMenu roleMenu : roleIdMap.get(roleId)) {
                List<MenuPrivilege> menuPrivileges = menuPrivilegeService.findByMenuId(roleMenu.getMenuId());
                privileges.addAll(menuPrivileges.stream().map(o -> privilegeMapper.map(o.getPrivilege())).collect(Collectors.toSet()));
            }
            rolePrivilegeService.store(roleId, privileges);
        }

//		return getAll();
    }

    public List<Long> defaultSetting(Long id, @Positive Long level) {
        // 레벨에 맞는 메뉴 ID 가져옴
        List<Long> collect = levelMenuService.findByLevelId(level).stream().map(LevelMenu::getMenuId).collect(Collectors.toList());

        List<RoleMenu> roleMenus = new ArrayList<>();
        for (Long menuId : collect) {
            roleMenus.add(RoleMenu.builder()
                    .roleId(id)
                    .menuId(menuId)
                    .modifier(securityUtils.getMemberId())
                    .modified(ZonedDateTime.now())
                    .build());
        }
        roleMenuService.saveAll(roleMenus);

        return collect;
    }
}
