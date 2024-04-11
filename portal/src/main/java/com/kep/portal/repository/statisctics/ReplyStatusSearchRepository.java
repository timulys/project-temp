package com.kep.portal.repository.statisctics;

import com.kep.portal.model.dto.statistics.GuestWaitingTimeAverageDto;
import com.kep.portal.model.dto.statistics.ReplyStatusDto;
import com.kep.portal.model.dto.statistics.TodaySummaryDto;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface ReplyStatusSearchRepository {

    List<ReplyStatusDto> findReplyStatusForBatch(ZonedDateTime start , ZonedDateTime end);

    TodaySummaryDto findTodaySummary(ZonedDateTime start , ZonedDateTime end , Long branchId , Long teamId , Long memberId);

}
