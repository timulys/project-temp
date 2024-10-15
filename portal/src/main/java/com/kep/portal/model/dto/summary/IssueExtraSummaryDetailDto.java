package com.kep.portal.model.dto.summary;

import com.kep.portal.model.dto.subject.IssueCategoryTreeDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "상담 요약 저장 DTO")
@NoArgsConstructor
@Data
public class IssueExtraSummaryDetailDto {
    @Schema(description = "채널 아이디")
    private Long channelId;
    @Schema(description = "채널명")
    private String channelName;

    @Schema(description = "상담 카테고리 뎁스")
    private Integer maxIssueCategoryDepth;
    @Schema(description = "상담 카테고리")
    private IssueCategoryTreeDto issueCategory;
    @Schema(description = "상담 요약 정보")
    private IssueExtraSummaryDto issueExtraSummary;


}
