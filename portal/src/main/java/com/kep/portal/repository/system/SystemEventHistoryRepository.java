package com.kep.portal.repository.system;


import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.system.SystemEventHistory;
import com.kep.portal.repository.issue.IssueSearchRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface SystemEventHistoryRepository extends JpaRepository<SystemEventHistory, Long> , SystemEventHistorySearchRepository {


}
