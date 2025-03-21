package com.kep.portal.model.dto.issue.summary;

import com.kep.portal.model.entity.issue.IssueSummaryCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class IssueSummaryCategoryDto {

    @Schema(description = "상담 후처리 카테고리 아이디")
    private Long issueSummaryCategoryId;
    @Schema(description = "상담 후처리 카테고리명")
    private String name;
    @Schema(description = "순서")
    private Integer sort;
    @Schema(description = "뎁스")
    private Integer depth;
    @Schema(description = "사용여부")
    private Boolean enabled;

    @Schema(description = "부모 후처리 카테고리 아이디")
    private Long parentId;
    @Schema(description = "자식 노드 리스트")
    private List<IssueSummaryCategoryDto> children;

    @Schema(description = "생성자")
    private Long creator;
    @Schema(description = "생성일시")
    private ZonedDateTime createdAt;
    @Schema(description = "수정자")
    private Long modifier;
    @Schema(description = "수정일시")
    private ZonedDateTime modifiedAt;



    public static IssueSummaryCategoryDto from(IssueSummaryCategory entity) {
        return IssueSummaryCategoryDto.builder()
                .issueSummaryCategoryId(entity.getId())
                .name(entity.getName())
                .sort(entity.getSort())
                .depth(entity.getDepth())
                .enabled(entity.getEnabled())
                .parentId(entity.getParent() == null ? null : entity.getParent().getId())
                .children(new ArrayList<>())
                .creator(entity.getCreator())
                .createdAt(entity.getCreatedAt())
                .modifier(entity.getModifier())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }
}
