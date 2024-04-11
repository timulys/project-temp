package com.kep.portal.service.privilege;

import com.kep.portal.model.entity.privilege.Privilege;
import com.kep.portal.model.entity.site.Menu;
import com.kep.portal.model.entity.privilege.MenuPrivilege;
import com.kep.portal.repository.privilege.MenuPrivilegeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * {@link Menu}, {@link Privilege} 매칭
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class MenuPrivilegeService {

	@Resource
	private MenuPrivilegeRepository menuPrivilegeRepository;

	public List<MenuPrivilege> findByMenuId(@NotNull @Positive Long menuId) {

		return menuPrivilegeRepository.findAll(Example.of(MenuPrivilege.builder()
				.menuId(menuId).build()));
	}

    public List<MenuPrivilege> findAllByMenuIdIn(List<Long> menuIds) {
		return menuPrivilegeRepository.findAllByMenuIdIn(menuIds);
    }
}
