package com.kep.portal.repository.privilege;

import com.kep.portal.model.entity.privilege.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {

	List<RolePrivilege> findAllByRoleId(@NotNull @Positive Long roleId);

	List<RolePrivilege> findAllByRoleIdIn(@NotEmpty Collection<Long> roleIds);

	void deleteAllByRoleId(@NotNull @Positive Long roleId);
}
