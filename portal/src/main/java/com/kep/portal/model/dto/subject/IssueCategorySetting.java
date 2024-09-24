package com.kep.portal.model.dto.subject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Schema(description = "상담 카테고리 정보")
@Data
public class IssueCategorySetting {

    @Schema(description = "채널 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    @PositiveOrZero
    private Long channelId;

    @Schema(description = "상담 카테고리 목록")
    private List<IssueCategoryTreeDto> issueCategories;

//    @Schema(description = "비활성화 상담 카테고리 아이디 목록")
//    private List<Long> unableIssueCategoryIds;

//    @Schema(description = "삭제 상담 카테고리 아이디 목록")
//    private List<Long> deleteIds;

}
