package com.kep.portal.model.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 통계 검색 조건
 */
@Schema(description = "통계 검색 조건")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsSearchDto {
	@Schema(description = "시작일 (yyyyMMdd)")
	private String startTime;
	@Schema(description = "종료일 (yyyyMMdd)")
	private String endTime;
	@Schema(description = "브랜치 아이디")
	private Long branchId;
}
