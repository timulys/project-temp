package com.kep.portal.repository.issue;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.portal.model.dto.issue.IssueSearchCondition;
import com.kep.portal.model.dto.statistics.TodaySummaryDto;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IssueSearchRepository {

	Page<Issue> search(@NotNull IssueSearchCondition condition, @NotNull Pageable pageable);

	Map<Long, Long> countByStatusInGroupByMember(@NotNull Collection<IssueStatus> statuses, @NotNull Collection<Long> memberIds);
	Long count(@NotNull IssueSearchCondition condition);

	List<Customer> latestCustomers(@NotNull Long memberId);


}
