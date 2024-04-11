package com.kep.portal.service.site;

import com.kep.portal.model.dto.site.MenuDto;
import com.kep.portal.model.entity.privilege.Role;
import com.kep.portal.model.entity.privilege.RoleMenu;
import com.kep.portal.model.entity.site.Menu;
import com.kep.portal.model.entity.site.MenuMapper;
import com.kep.portal.repository.site.MenuRepository;
import com.kep.portal.service.privilege.RoleMenuService;
import com.kep.portal.service.privilege.RoleService;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class MenuService {

	@Resource
	private MenuRepository menuRepository;
	@Resource
	private MenuMapper menuMapper;
	@Resource
	private SecurityUtils securityUtils;
	@Resource
	private RoleService roleService;
	@Resource
	private RoleMenuService roleMenuService;

	public List<MenuDto> getGnb() {

		List<Menu> menus;

		if (securityUtils.isMaster()) {
			menus = menuRepository.findAllByEnabledAndMasterEnabledAndDepthOrderBySortAsc(true, true, 1);
		} else {
			List<Long> menuIds = getMenuIds();
			menus = menuRepository.findAllByIdInAndEnabledAndDepthOrderBySortAsc(menuIds, true, 1);
		}

		return menuMapper.map(menus);
	}

	public List<MenuDto> getLnb(@NotNull Long topId) {

		List<Long> menuIds = getMenuIds();
		List<Menu> menus;

		if (securityUtils.isMaster()) {
			menus = menuRepository.findAllByEnabledAndMasterEnabledAndTopIdOrderBySortAsc(true, true, topId);
//			List<Menu> roleDisabledMenus = menuRepository.findAll(Example.of(Menu.builder()
//					.enabled(true)
//					.roleEnabled(false)
//					.build()));
//			log.info("roleDisabledMenus: {}", roleDisabledMenus.stream().map(Menu::getId).collect(Collectors.toList()));
//			menuIds.addAll(roleDisabledMenus.stream().map(Menu::getId).collect(Collectors.toList()));
		} else {
			menus = menuRepository.findAllByIdInAndEnabledAndTopIdOrderBySortAsc(menuIds, true, topId);
		}

		return menuMapper.map(menus);
	}

	private List<Long> getMenuIds() {

		List<String> roleTypes = new ArrayList<>();
		for (String role : securityUtils.getRoles()) {
			if (role.startsWith("ROLE_")) {
				roleTypes.add(role.replace("ROLE_", ""));
			}
		}

		List<Role> roles = roleService.findAllByTypeIn(roleTypes);
		log.info("ROLES: {}", roles.stream().map(Role::getType).collect(Collectors.toList()));
		List<RoleMenu> roleMenus = roleMenuService.findAllByRoleIdIn(roles.stream().map(Role::getId).collect(Collectors.toList()));
		log.info("ROLE MENUS: {}", roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList()));
		return roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
	}
}
