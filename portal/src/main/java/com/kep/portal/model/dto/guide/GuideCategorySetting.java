package com.kep.portal.model.dto.guide;


import com.kep.core.model.dto.guide.GuideCategoryDto;
import com.kep.portal.model.entity.guide.GuideCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "가이드 카테고리 세팅 정보")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideCategorySetting {

    @Schema(description = "브랜치 아이디")
    private Long branchId;


    @Schema(description = "업데이트 가이드 카테고리 목록")
    List<GuideCategoryDto> update;
    @Schema(description = "추가 가이드 카테고리 목록")
    List<GuideCategoryDto> create;
    @Schema(description = "삭제 가이드 카테고리 아이디 목록")
    List<Long> delete;
}
