package com.kep.portal.repository.issue;

import com.kep.core.model.dto.issue.IssueSupportStatus;
import com.kep.core.model.dto.issue.IssueSupportType;
import com.kep.portal.model.entity.issue.IssueSupportHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface IssueSupportHistoryRepository extends JpaRepository<IssueSupportHistory, Long> {


    List<IssueSupportHistory> findByIssueIdOrderByCreated(@NotNull Long issueId);
}
