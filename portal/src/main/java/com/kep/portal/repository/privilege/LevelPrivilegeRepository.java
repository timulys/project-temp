package com.kep.portal.repository.privilege;

import com.kep.portal.model.entity.privilege.LevelPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelPrivilegeRepository extends JpaRepository<LevelPrivilege, Long> {
}
