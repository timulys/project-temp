package com.kep.portal.service.privilege;

import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.model.entity.privilege.LevelMenu;
import com.kep.portal.model.entity.site.Menu;
import com.kep.portal.repository.privilege.LevelMenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * {@link Level}, {@link Menu} 매칭
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class LevelMenuService {

	@Resource
	private LevelMenuRepository levelMenuRepository;

	public List<LevelMenu> findByLevelId(@NotNull @Positive Long levelId) {

		return levelMenuRepository.findAll(Example.of(LevelMenu.builder()
				.levelId(levelId).build()));
	}
}
