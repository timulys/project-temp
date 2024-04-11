package com.kep.portal.service.assign;

import com.kep.portal.model.entity.assign.AssignMethod;
import com.kep.portal.repository.issue.AssignMethodRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AssignMethodService {

	@Resource
	private AssignMethodRepository assignMethodRepository;

	public List<AssignMethod> findAll() {

		return assignMethodRepository.findAll(Sort.by(Sort.Direction.ASC, "sort"));
	}
}
