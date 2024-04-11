package com.kep.portal.repository.issue;

import com.kep.portal.model.dto.issue.IssueLogSearchCondition;
import com.kep.portal.model.entity.issue.IssueLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

public interface IssueLogSearchRepository {

	Page<IssueLog> search(@NotNull IssueLogSearchCondition condition, @NotNull Pageable pageable);
}
