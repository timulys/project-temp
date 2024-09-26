package com.kep.portal.model.dto.subject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class IssueCategoryResponse {

    @Schema(description = "상담 카테고리 설정 단계 (0일경우 미설정)")
    private Integer maxDepth;

    @Schema(description = "상담 카테고리 트리")
    private List<IssueCategoryTreeDto> issueCategories;

    public IssueCategoryResponse() {}

    public IssueCategoryResponse(Integer maxDepth, List<IssueCategoryTreeDto> issueCategories) {
        this.maxDepth = maxDepth;
        this.issueCategories = issueCategories;
    }
}
