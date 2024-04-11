package com.kep.portal.repository.privilege;

import com.kep.portal.model.entity.privilege.LevelMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelMenuRepository extends JpaRepository<LevelMenu, Long> {
}
