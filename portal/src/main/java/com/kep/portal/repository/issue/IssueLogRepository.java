package com.kep.portal.repository.issue;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kep.core.model.dto.issue.IssueLogStatus;
import com.kep.portal.model.entity.issue.IssueLog;

@Repository
public interface IssueLogRepository extends JpaRepository<IssueLog, Long>, IssueLogSearchRepository {

	Page<IssueLog> findAllByIssueId(@NotNull Long issueId, Pageable pageable);

	long countByIssueIdAndCreatorAndCreatedAfter(@NotNull Long issueId, @NotNull Long creator, @NotNull ZonedDateTime created);

	IssueLog findFirstByIssueIdOrderByCreatedDesc(@NotNull Long issueId);
	
	boolean existsByIssueIdAndStatusAndCreatorLessThan(Long issueId, IssueLogStatus status, Long creator);
}
