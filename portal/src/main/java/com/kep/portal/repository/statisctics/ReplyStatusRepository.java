package com.kep.portal.repository.statisctics;

import java.util.List;

import com.kep.portal.model.dto.statistics.ReplyStatusDto;
import com.kep.portal.model.dto.statistics.TodaySummaryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kep.portal.model.entity.statistics.ReplyStatus;

//@Repository
public interface ReplyStatusRepository extends JpaRepository<ReplyStatus, Long> , ReplyStatusSearchRepository {
//	@Query(nativeQuery = true)
//	List<ReplyStatusDto> findReplyStatusForBatch(@Param("startTime") String startTime, @Param("endTime") String endTime);

	ReplyStatus findByTimeGroupAndBranchIdIsNull(String timeGroup);

	ReplyStatus findByTimeGroupAndBranchId(String timeGroup, Long branchId);

	List<ReplyStatus> findByTimeGroupGreaterThanEqualAndTimeGroupLessThan(String stratDateTime, String endDateTime);
	
	List<ReplyStatus> findByTimeGroupGreaterThanEqualAndTimeGroupLessThanAndBranchId(String stratDateTime, String endDateTime, Long branchId);
}
