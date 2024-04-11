package com.kep.portal.repository.statisctics;

import com.kep.portal.model.dto.statistics.GuestWaitingTimeAverageDto;
import com.kep.portal.model.entity.statistics.GuestWaitingTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface GuestWaitingTimeSearchRepository {

	List<GuestWaitingTimeAverageDto> findAverageReplyTimesGroupBy3Week(Long branchId);

	GuestWaitingTimeAverageDto findAverageReplyTimesBy3Week();

	GuestWaitingTimeAverageDto findAverageReplyTimesBy3WeekOfBranch(@Param("branchId") Long branchId);
}
