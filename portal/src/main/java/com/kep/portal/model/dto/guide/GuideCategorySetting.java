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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideCategorySetting {

    @Schema(description = "브랜치 아이디")
    private Long branchId;


    @Schema(description = "")
    List<GuideCategoryDto> update;
    @Schema(description = "")
    List<GuideCategoryDto> create;
    @Schema(description = "")
    List<Long> delete;
}
