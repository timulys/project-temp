package com.kep.portal.service.privilege;

import com.kep.portal.model.dto.privilege.LevelDto;
import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.model.entity.privilege.LevelMapper;
import com.kep.portal.repository.privilege.LevelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class LevelService {

	@Resource
	private LevelRepository levelRepository;
	@Resource
	private LevelMapper levelMapper;

	public Level findById(@NotNull @Positive Long id) {

		return levelRepository.findById(id).orElse(null);
	}

	public List<LevelDto> getAll() {

		List<Level> levels = levelRepository.findAll();
		return levelMapper.map(levels);
	}
}
