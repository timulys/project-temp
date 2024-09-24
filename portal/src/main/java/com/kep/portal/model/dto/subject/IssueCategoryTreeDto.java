package com.kep.portal.model.dto.subject;

import com.kep.portal.model.entity.subject.IssueCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.checkerframework.checker.index.qual.Positive;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@ToString
@Data
public class IssueCategoryTreeDto {

    @Schema(description = "이슈 카테고리 아이디")
    @Nullable
    @Positive
    private Long issueCategoryId;

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

    @Schema(description = "하위 카테고리")
    private List<IssueCategoryTreeDto> children;

    public IssueCategoryTreeDto() {}

    private IssueCategoryTreeDto(IssueCategory issueCategory) {
        issueCategoryId = issueCategory.getId();
        name = issueCategory.getName();
        parentId = issueCategory.getParent().getId();
        exposed = issueCategory.getExposed();
        depth = issueCategory.getDepth();
        sort = issueCategory.getSort();
        enabled = issueCategory.getEnabled();
        children = new ArrayList<>();
    }

    public static IssueCategoryTreeDto of(IssueCategory issueCategory) {
        return new IssueCategoryTreeDto(issueCategory);
    }
}
