package com.kep.portal.service.issue;

import com.kep.portal.model.entity.issue.IssueStorage;
import com.kep.portal.model.type.IssueStorageType;
import com.kep.portal.repository.issue.IssueStorageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@Slf4j
public class IssueStorageService {

	@Resource
	private IssueStorageRepository issueStorageRepository;

	@Nullable
	public IssueStorage findOne(@NotNull @Positive Long issueId, @NotNull IssueStorageType issueStorageType) {

		return findOne(issueId, issueStorageType, true);
	}

	@Nullable
	public IssueStorage findOne(@NotNull @Positive Long issueId, @NotNull IssueStorageType issueStorageType, boolean enabled) {

		return issueStorageRepository.findOne(Example.of(IssueStorage.builder()
				.issueId(issueId)
				.type(issueStorageType)
				.enabled(enabled)
				.build())).orElse(null);
	}

	public List<IssueStorage> findAllByIssueId(@NotNull @Positive Long issueId) {

		return issueStorageRepository.findAll(Example.of(IssueStorage.builder()
				.issueId(issueId)
				.enabled(true)
				.build()));
	}

	public Page<IssueStorage> findAll(@NotNull Example<IssueStorage> example, @NotNull Pageable pageable) {

		return issueStorageRepository.findAll(example, pageable);
	}

	public IssueStorage save(IssueStorage entity) {

		return issueStorageRepository.save(entity);
	}

	public void deleteAll(@NotEmpty Collection<IssueStorage> entities) {

		entities.forEach(o -> o.setEnabled(false));
		issueStorageRepository.saveAll(entities);
	}

	public void delete(@NotNull IssueStorage entity) {

		issueStorageRepository.delete(entity);
		issueStorageRepository.flush();
	}
}
