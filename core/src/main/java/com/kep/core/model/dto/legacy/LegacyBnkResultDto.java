package com.kep.core.model.dto.legacy;

import com.kep.core.model.dto.issue.IssueExtraDto;
import com.kep.core.model.dto.subject.IssueCategoryDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BNK 기간계 인입결과 전송 API 정보
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LegacyBnkResultDto {
	private IssueExtraDto issueExtraDto;
    private LegacyBnkCategoryDto legacyDto;
    private IssueCategoryDto issueCategoryDto;
}
