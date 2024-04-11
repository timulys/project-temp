package com.kep.portal.repository.privilege;

import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.model.entity.privilege.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

//	Optional<Role> findByType(@NotEmpty String type);

	List<Role> findAllByTypeIn(@NotEmpty Collection<String> types);

	List<Role> findAllByLevel(@NotNull Level level);

	List<Role> findAllByLevelIn(@NotNull Collection<Level> level);

	List<Role> findAllByIdIn(@NotNull Collection<Long> ids);
}
