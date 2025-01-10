package com.kep.portal.model.dto.issue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
@Data
public class IssueSummaryCategoryDto {

    @Schema(description = "")
    private Long issueSummaryCategoryId;
    @Schema(description = "")
    private String name;
    @Schema(description = "")
    private Integer sort;
    @Schema(description = "")
    private Integer depth;
    @Schema(description = "")
    private Boolean enabled;

    @Schema(description = "")
    private Long parentId;
    @Schema(description = "")
    private List<IssueSummaryCategoryDto> children;

    @Schema(description = "")
    private Long creator;
    @Schema(description = "")
    private ZonedDateTime createdAt;
    @Schema(description = "")
    private Long modifier;
    @Schema(description = "")
    private ZonedDateTime modifiedAt;

}
