package com.kep.portal.model.dto.subject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Schema(description = "상담 카테고리 정보")
@Data
public class IssueCategorySetting {

    @Schema(description = "채널 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    @Positive
    private Long channelId;

    @Schema(description = "상담 카테고리 목록")
    @Valid
    private List<IssueCategoryTreeDto> issueCategories;

}
