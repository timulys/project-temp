package com.kep.portal.model.dto.subject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 이슈 분류 (관리 > 저장)
 */
@Schema(description = "이슈 카테고리 저장")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueCategoryStoreDto {

	@Schema(description = "이슈 카테고리명")
	private String name;

	@Schema(description = "상위 카테고리 아이디")
	private Long parentId;

	@Schema(description = "")
	private Boolean exposed;

	@Schema(description = "정렬 변수")
	private Integer sort;

	@Schema(description = "사용여부")
	private Boolean enabled;
}
