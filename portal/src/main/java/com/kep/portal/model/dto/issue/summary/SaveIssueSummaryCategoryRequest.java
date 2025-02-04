package com.kep.portal.model.dto.issue.summary;

import com.kep.portal.model.entity.issue.IssueSummaryCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class SaveIssueSummaryCategoryRequest {
    @Schema(description = "")
    @Nullable
    private Long issueSummaryCategoryId;

    @Schema(description = "")
    @NotBlank
    private String name;

    @Schema(description = "")
    @Range(min = 1)
    @Positive
    private Integer sort;

    @Schema(description = "단계")
    @Range(min = 1, max = 3)
    @Positive
    private Integer depth;

    @Schema(description = "")
    @NotNull
    private Boolean enabled;

    @Schema(description = "")
    @Nullable
    private Long parentId;

    public IssueSummaryCategory toEntity(Long memberId, IssueSummaryCategory parent) {
        return IssueSummaryCategory.create(
                issueSummaryCategoryId,
                parent,
                name,
                sort,
                depth,
                enabled,
                memberId
        );
    }
}
