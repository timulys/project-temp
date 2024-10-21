package com.kep.portal.model.dto.issue;

import com.kep.core.model.dto.issue.IssueSupportStatus;
import com.kep.core.model.dto.issue.IssueSupportType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 상담지원요청 검색
 */
@Schema(description = "상담지원요청 검색")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueSupportSearchDto {

    @Schema(description = "조회 시작일", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String searchStartDate;

    @Schema(description = "조회 종료일", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String searchEndDate;

    @Schema(description = "상담 지원 타입 :: change : 상담 직원 전환 요청, question : 상담 검토 요청", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<IssueSupportType> type;

    @Schema(description = "브랜치 아이디", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long branchId;

    @Schema(description = "상담그룹 아이디", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long teamId;

    @Schema(description = "사용자 아이디", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long memberId;

    @Schema(description = "상담 지원 상태(다건) [request : 상담검토/상담직원전환 요청, reject : 반려, finish : 완료, change : 상담직원변경, receive : 상담이어받기, auto : 전환자동승인, end : 상담종료]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<IssueSupportStatus> status;

}
