package com.kep.portal.model.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyStatusDto {


    private Long entryCount;// 인입건수

    private Long replyCount;// 응답건수

    private Long branchId;// 지점 Id

    private Long averageCounselTime;// 상담소요시간

    private String timeGroup; // 고객 인입 일시 그룹(10분 간격), 예시 : 2023년 3월 22일 9시 10분대 202303220910

    private Long missingCount;// 상담 놓침 건수
}
