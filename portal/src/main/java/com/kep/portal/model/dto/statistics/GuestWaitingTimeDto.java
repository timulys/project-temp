package com.kep.portal.model.dto.statistics;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class GuestWaitingTimeDto {
	private Long issueId;
	private Long branchId;
	// 고객 인입 일시
	private ZonedDateTime entryTime;
	// 대화 시작 일시
	private ZonedDateTime firstReplyTime;
	// 고객 인입 일시 그룹(10분 간격), 예시 : 2023년 3월 22일 9시 10분대 202303220910
	private String entryTimeGroup;
	// entryTime ~ firstReplyTime 초
	private Long waitingTime;
}
