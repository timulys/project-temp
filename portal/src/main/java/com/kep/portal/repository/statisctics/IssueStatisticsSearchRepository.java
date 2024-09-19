package com.kep.portal.repository.statisctics;

import com.kep.portal.model.dto.statistics.IssueStatisticsDto;
import com.kep.portal.model.dto.issue.IssueMemberStatisticsDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public interface IssueStatisticsSearchRepository {
    List<IssueStatisticsDto> search(@NotNull LocalDate from, @NotNull LocalDate to, Long branchId , Long teamId , Long memberId);

    IssueStatisticsDto searchAllPeriod (@NotNull LocalDate from, @NotNull LocalDate to, Long branchId , Long teamId , Long memberId);

    List<IssueMemberStatisticsDto> members(@NotNull ZonedDateTime from, @NotNull ZonedDateTime to, @NotNull Long branchId , Long teamId);
}
