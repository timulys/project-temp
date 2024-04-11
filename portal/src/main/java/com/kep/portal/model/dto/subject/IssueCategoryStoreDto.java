package com.kep.portal.model.dto.subject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 이슈 분류 (관리 > 저장)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueCategoryStoreDto {

	private String name;

	private Long parentId;

	private Boolean exposed;

	private Integer sort;

	private Boolean enabled;
}
