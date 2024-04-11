package com.kep.portal.repository.statisctics;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kep.portal.model.dto.statistics.GuestWaitingTimeAverageDto;
import com.kep.portal.model.entity.statistics.GuestWaitingTime;

public interface GuestWaitingTimeRepository extends JpaRepository<GuestWaitingTime, Long> ,GuestWaitingTimeSearchRepository{
	// 날짜로 사용자 대기 시간 검색
	List<GuestWaitingTime> findByEntryTimeGroupGreaterThanEqualAndEntryTimeGroupLessThan(String startTime, String endTime);

	// 날짜, branch ID로 사용자 대기 시간 검색
	List<GuestWaitingTime> findByEntryTimeGroupGreaterThanEqualAndEntryTimeGroupLessThanAndBranchId(String startTime, String endTime, Long branchId);

	// 사용자 평균 대기 시간 검색 3개월 별
	@Query(nativeQuery = true)
	List<GuestWaitingTimeAverageDto> findAverageReplyTimesGroupBy3Month();

	// Branch 사용자 평균 대기 시간 검색 3개월 별
	@Query(nativeQuery = true)
	List<GuestWaitingTimeAverageDto> findAverageReplyTimesGroupBy3MonthOfBranch(@Param("branchId") Long branchId);
	
	//사용자 평균 대기 시간 검색 3주 별
//	@Query(nativeQuery = true)
//	List<GuestWaitingTimeAverageDto> findAverageReplyTimesGroupBy3Week();

	//Branch 사용자 평균 대기 시간 검색 3주 별
	@Query(nativeQuery = true)
	List<GuestWaitingTimeAverageDto> findAverageReplyTimesGroupBy3WeekOfBranch(@Param("branchId") Long branchId);
	
	// 사용자 평균 대기 시간 검색 3개월 평균
	@Query(nativeQuery = true)
	GuestWaitingTimeAverageDto findAverageReplyTimesBy3Month();

//사용자 평균 대기 시간 검색 3개월 평균
	@Query(nativeQuery = true)
	GuestWaitingTimeAverageDto findAverageReplyTimesBy3MonthOfBranch(@Param("branchId") Long branchId);
	
	//사용자 평균 대기 시간 검색 3주 평균
//	@Query(nativeQuery = true)
//	GuestWaitingTimeAverageDto findAverageReplyTimesBy3Week();
	
//사용자 평균 대기 시간 검색 3개월 평균
//	@Query(nativeQuery = true)
//	GuestWaitingTimeAverageDto findAverageReplyTimesBy3WeekOfBranch(@Param("branchId") Long branchId);
}
