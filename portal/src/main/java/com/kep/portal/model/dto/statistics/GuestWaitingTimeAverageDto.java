package com.kep.portal.model.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Builder
public class GuestWaitingTimeAverageDto {
	private String dateRange; // 연월(주일)
	private Long waitingTimeAverage;// 평균 대기 시간
}
