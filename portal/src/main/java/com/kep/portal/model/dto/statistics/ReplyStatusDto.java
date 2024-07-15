package com.kep.portal.model.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyStatusDto {


    @Schema(description = "인입건수")
    private Long entryCount;// 인입건수

    @Schema(description = "응답건수")
    private Long replyCount;// 응답건수

    @Schema(description = "지점 Id")
    private Long branchId;// 지점 Id

    @Schema(description = "상담소요시간")
    private Long averageCounselTime;// 상담소요시간

    @Schema(description = "고객 인입 일시 그룹(10분 간격), 예시 : 2023년 3월 22일 9시 10분대 202303220910")
    private String timeGroup; // 고객 인입 일시 그룹(10분 간격), 예시 : 2023년 3월 22일 9시 10분대 202303220910

    @Schema(description = "상담 놓침 건수")
    private Long missingCount;// 상담 놓침 건수
}
