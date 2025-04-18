package com.kep.portal.model.dto.team.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PostBranchTeamRequestDto {
    @Schema(description = "상담그룹명")
    private String name;
    @Schema(description = "상담그룹장 ID")
    private Long memberId;
    @Schema(description = "상담그룹 브랜치 ID")
    private Long branchId;
}
