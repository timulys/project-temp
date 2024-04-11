package com.kep.portal.service.privilege;

import com.kep.portal.model.entity.privilege.*;
import com.kep.portal.model.entity.site.Menu;
import com.kep.portal.repository.privilege.RoleMenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * {@link Role}, {@link Menu} 매칭
 */
@Service
@Transactional
@Slf4j
public class RoleMenuService {

	@Resource
	private RoleMenuRepository roleMenuRepository;

	public List<RoleMenu> findAllByMenuId(@NotNull @Positive Long menuId) {

		return roleMenuRepository.findAll(Example.of(RoleMenu.builder()
				.menuId(menuId).build()));
	}

	public List<RoleMenu> findAllByRoleIdIn(@NotEmpty List<Long> roleIds) {

		return roleMenuRepository.findAllByRoleIdIn(roleIds);
	}

	public List<RoleMenu> saveAll(@NotEmpty List<RoleMenu> roleMenus) {

		roleMenus = roleMenuRepository.saveAll(roleMenus);
		roleMenuRepository.flush();
		return roleMenus;
	}

	public void deleteAll() {

		roleMenuRepository.deleteAll();
		roleMenuRepository.flush();
	}
}
