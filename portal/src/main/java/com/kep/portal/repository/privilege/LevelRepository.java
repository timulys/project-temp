package com.kep.portal.repository.privilege;

import com.kep.portal.model.entity.privilege.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

    List<Level> findAllByTypeIn(Collection<String> types);
}
