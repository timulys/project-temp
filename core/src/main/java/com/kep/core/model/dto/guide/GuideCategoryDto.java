package com.kep.core.model.dto.guide;

import com.fasterxml.jackson.annotation.*;
import com.kep.core.model.dto.branch.BranchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideCategoryDto {

    @Positive
    @NotNull
    @Schema(description = "가이드 카테고리 아이디")
    private Long id;


    @Schema(description = "가이드 카테고리명")
    private String name;


    @Schema(description = "하위 카테고리")
    private List<GuideCategoryDto> children;


    @JsonIncludeProperties({"id", "name"})
    @Schema(description = "브랜치 정보")
    private BranchDto branch;

    @Schema(description = "브랜치 아이디")
    private Long branchId;

    @Schema(description = "브랜치 전체 오픈 여부")
    private Boolean isOpen = false;

    @Schema(description = "상위 카테고리 아이디")
    private Long parentId;

    @Positive
    @Schema(description = "카테고리 뎁스")
    private Integer depth;

    @Positive
    @Schema(description = "생성자")
    private Long creator;

    @Schema(description = "생성일시")
    private ZonedDateTime created;

    @Positive
    @Schema(description = "수정자")
    private Long modifier;


    @Schema(description = "수정일시")
    private ZonedDateTime modified;
}
