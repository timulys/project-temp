package com.kep.portal.service.privilege;

import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.model.entity.privilege.LevelPrivilege;
import com.kep.portal.model.entity.privilege.Privilege;
import com.kep.portal.repository.privilege.LevelPrivilegeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * {@link Level}, {@link Privilege} 매칭
 */
@Deprecated
@Service
@Transactional(readOnly = true)
@Slf4j
public class LevelPrivilegeService {

	@Resource
	private LevelPrivilegeRepository levelPrivilegeRepository;

	public List<LevelPrivilege> findByLevelId(@NotNull @Positive Long levelId) {

		return levelPrivilegeRepository.findAll(Example.of(LevelPrivilege.builder()
				.levelId(levelId).build()));
	}
}
