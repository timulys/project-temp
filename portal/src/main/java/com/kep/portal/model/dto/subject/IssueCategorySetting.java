package com.kep.portal.model.dto.subject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class IssueCategorySetting {

    @Schema(description = "상담 카테고리 목록")
    @Valid
    private List<IssueCategoryTreeDto> issueCategories;

}
