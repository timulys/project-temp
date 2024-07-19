package com.kep.portal.model.dto.subject;

import com.kep.portal.model.entity.subject.IssueCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.checkerframework.checker.index.qual.Positive;

import javax.annotation.Nullable;

@Data
public class IssueCategorySaveDto {

    @Schema(description = "이슈 카테고리 아이디")
    @Nullable
    @Positive
    private Long categoryId;

    @Schema(description = "이슈 카테고리명")
    private String name;

    @Schema(description = "상위 카테고리 아이디")
    private Long parentId;

    @Schema(description = "전체 오픈여부")
    private Boolean exposed;

    @Schema(description = "단계")
    @Positive
    private Integer depth;

    @Schema(description = "정렬 변수")
    @Positive
    private Integer sort;

    @Schema(description = "사용여부")
    private Boolean enabled;

}
