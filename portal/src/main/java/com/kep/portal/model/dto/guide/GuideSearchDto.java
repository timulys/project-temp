package com.kep.portal.model.dto.guide;

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

    private String keyword;

    private Long branchId;

    private Long teamId;

    private Long categoryId;

    @Enumerated(EnumType.STRING)
    private SearchType type;

    public enum SearchType{
        message,
        name,
        file
    }
}
