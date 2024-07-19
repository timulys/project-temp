package com.kep.core.model.dto.subject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 이슈 분류
 */
@Schema(description = "이슈 분류")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueCategoryDto {

	@Positive
	@Schema(description = "이슈 카테고리 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long id;

	@Schema(description = "이슈 카테고리명")
	private String name;

	@Positive
	@Schema(description = "브랜치 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long branchId;

	@JsonIgnoreProperties({"path"})
	@Schema(description = "상위 단계 카테고리 정보")
	private IssueCategoryBasicDto parent;

	@Positive
	@Schema(description = "뎁스")
	private Integer depth;

	@Schema(description = "")
	private Boolean exposed;

	/**
	 * FIXME :: bnk 20240715 volka
	 */
	@Schema(description = "bnk 코드(제거)")
	private String bnkCode;

	@JsonIgnoreProperties({"parent", "path"})
	@Schema(description = "이슈 카테고리 경로별 정보")
	private List<IssueCategoryBasicDto> path;
	
	public static class IssueCategoryDtoBuilder {}
}
