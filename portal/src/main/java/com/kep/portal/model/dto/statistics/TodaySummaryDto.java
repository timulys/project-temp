package com.kep.portal.model.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "금일 상담통계 요약 현황")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodaySummaryDto {

    // 고객 수
    @Schema(description = "고객 수")
    private Long guestCount;
    // 상담 진행수
    @Schema(description = "상담 진행수")
    private Long counselingCount;
    // 대기수
    @Schema(description = "대기수")
    private Long waitingCount;
    //놓침 수
    @Schema(description = "놓침 수")
    private Long missingCount;
    // 지연 수
    @Schema(description = "지연 수")
    private Long delayCount;
    //상담 완료 수
    @Schema(description = "상담 완료 수")
    private Long closedCount;
    //상담원수
    @Schema(description = "상담원수")
    private Long memberCount;
}
