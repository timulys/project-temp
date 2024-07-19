package com.kep.portal.model.dto.subject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Schema(description = "상담 카테고리 정보")
@Data
public class IssueCategorySetting {

    @Schema(description = "채널 아이디")
    @PositiveOrZero
    private Long channelId;

    @Schema(description = "상담 카테고리 저장 목록")
    @Valid
    private List<IssueCategorySaveDto> issueCategories;

//    @Schema(description = "삭제 상담 카테고리 아이디 목록")
//    private List<Long> deleteIds;

}
