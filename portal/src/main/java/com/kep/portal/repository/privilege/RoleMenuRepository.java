package com.kep.portal.repository.privilege;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kep.portal.model.entity.privilege.RoleMenu;

@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {

	//List<RoleMenu> findAllByMenuId(@NotNull @Positive Long menuId);

	List<RoleMenu> findAllByRoleIdIn(@NotEmpty List<Long> roleIds);
	
	List<RoleMenu> findAllByMenuId(Long nenuId);

	List<RoleMenu> findAllByRoleId(Long roleId);

	Boolean existsByRoleIdAndMenuId(Long roleId, Long menuId);

	List<RoleMenu> findAllByRoleIdIn(@NotNull Set<Long> roleIds);

	void deleteAllByRoleId(@NotNull Long roleId);
}
