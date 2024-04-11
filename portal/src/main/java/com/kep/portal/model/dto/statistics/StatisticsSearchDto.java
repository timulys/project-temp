package com.kep.portal.model.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 통계 검색 조건
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsSearchDto {
	private String startTime;
	private String endTime;
	private Long branchId;	
}
