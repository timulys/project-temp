package com.kep.platform.service;

import com.kep.platform.model.entity.PlatformSession;
import com.kep.platform.repository.PlatformSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;

@Service
@Transactional
@Slf4j
public class PlatformSessionService {

	@Resource
	private PlatformSessionRepository platformSessionRepository;

	public PlatformSession findById(@NotEmpty String id) {

		return platformSessionRepository.findById(id).orElse(null);
	}

	public PlatformSession save(PlatformSession entity) {

		return platformSessionRepository.save(entity);
	}
}
