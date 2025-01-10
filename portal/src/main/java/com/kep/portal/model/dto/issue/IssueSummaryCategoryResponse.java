package com.kep.portal.model.dto.issue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class IssueSummaryCategoryResponse {

    @Schema(description = "상담 요약(후처리) 카테고리 트리")
    private List<IssueSummaryCategoryDto> issueSummaryCategories;

    public IssueSummaryCategoryResponse(List<IssueSummaryCategoryDto> issueSummaryCategories) {
        this.issueSummaryCategories = issueSummaryCategories;
    }
}
