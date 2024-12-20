package com.kep.portal.model.dto.subject;

import com.kep.portal.model.entity.subject.IssueCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.checkerframework.checker.index.qual.Positive;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 상담 카테고리 트리용 DTO. 기존 IssueCategoryDto 등이 존재하여 해당 클래스 사용. 추후 고도화시 전환
 */
@ToString
@Data
public class IssueCategoryTreeDto {

    @Schema(description = "이슈 카테고리 아이디")
    @Nullable
    @Positive
    private Long issueCategoryId;

    @Schema(description = "이슈 카테고리명")
    @NotEmpty
    private String name;

    @Schema(description = "상위 카테고리 아이디")
    @Nullable
    private Long parentId;

    @Schema(description = "전체 오픈여부")
    @NotNull
    private Boolean exposed;

    @Schema(description = "단계 (1~3)")
    @NotNull
    @Positive
    @Min(value = 1)
    @Max(value = 3)
    private Integer depth;

    @Schema(description = "정렬 변수(1부터)")
    @NotNull
    @Positive
    @Min(value = 1)
    private Integer sort;

    @Schema(description = "사용여부")
    @NotNull
    private Boolean enabled;

    @Schema(description = "하위 카테고리")
    @Nullable
    @Valid
    private List<IssueCategoryTreeDto> children;

    public IssueCategoryTreeDto() {}

    private IssueCategoryTreeDto(IssueCategory issueCategory) {
        issueCategoryId = issueCategory.getId();
        name = issueCategory.getName();
        parentId = issueCategory.getParent() == null ? null : issueCategory.getParent().getId();
        exposed = issueCategory.getExposed();
        depth = issueCategory.getDepth();
        sort = issueCategory.getSort();
        enabled = issueCategory.getEnabled();
        children = new ArrayList<>();
    }

    public static IssueCategoryTreeDto of(IssueCategory issueCategory) {
        return new IssueCategoryTreeDto(issueCategory);
    }

    public IssueCategory toEntity(@NotNull Long memberId, @NotNull Long channelId, @Nullable IssueCategory parent) {
        IssueCategory issueCategory = new IssueCategory();
        issueCategory.setId(issueCategoryId);
        issueCategory.setName(name);
        issueCategory.setExposed(exposed);
        issueCategory.setDepth(depth);
        issueCategory.setSort(sort);
        issueCategory.setEnabled(enabled);
        issueCategory.setExposed(exposed);
        issueCategory.setParent(parent);
        issueCategory.setChannelId(channelId);
        issueCategory.setModifier(memberId);

        return issueCategory;
    }
}
