package com.kep.portal.repository.site;

import com.kep.portal.model.entity.site.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	List<Menu> findAllByIdInAndEnabledAndDepthOrderBySortAsc(List<Long> ids, Boolean enabled, Integer depth);

	List<Menu> findAllByIdInAndEnabledAndTopIdOrderBySortAsc(List<Long> ids, Boolean enabled, Long topId);

	List<Menu> findAllByEnabledAndMasterEnabledAndDepthOrderBySortAsc(Boolean enabled, Boolean masterEnabled, Integer depth);

	List<Menu> findAllByEnabledAndMasterEnabledAndTopIdOrderBySortAsc(Boolean enabled, Boolean masterEnabled, Long topId);
}
