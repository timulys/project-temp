package com.kep.portal.model.dto.guide;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


/**
 * 가이드 검색
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideSearchDto {

    @Schema(description = "키워드" , requiredMode = Schema.RequiredMode.REQUIRED)
    private String keyword;

    @Schema(description = "브랜치 아이디")
    private Long branchId;

    @Schema(description = "팀 아이디")
    private Long teamId;

    @Schema(description = "가이드 카테고리 아이디")
    private Long categoryId;

    @Schema(description = "검색 타입 (message, name, file)" , requiredMode = Schema.RequiredMode.REQUIRED)
    @Enumerated(EnumType.STRING)
    private SearchType type;

    public enum SearchType{
        message,
        name,
        file
    }
}
