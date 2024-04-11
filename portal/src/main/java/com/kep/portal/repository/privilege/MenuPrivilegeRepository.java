package com.kep.portal.repository.privilege;

import com.kep.portal.model.entity.privilege.MenuPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

@Repository
public interface MenuPrivilegeRepository extends JpaRepository<MenuPrivilege, Long> {

	List<MenuPrivilege> findAllByMenuId(@NotNull @Positive Long menuId);

	List<MenuPrivilege> findAllByMenuIdIn(@NotEmpty Collection<Long> menuIds);

	void deleteAllByMenuId(@NotNull @Positive Long menuId);
}
