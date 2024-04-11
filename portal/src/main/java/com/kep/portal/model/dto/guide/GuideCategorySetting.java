package com.kep.portal.model.dto.guide;


import com.kep.core.model.dto.guide.GuideCategoryDto;
import com.kep.portal.model.entity.guide.GuideCategory;
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

    private Long branchId;

    List<GuideCategoryDto> update;
    List<GuideCategoryDto> create;
    List<Long> delete;
}
