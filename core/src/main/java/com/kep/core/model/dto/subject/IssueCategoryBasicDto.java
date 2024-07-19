package com.kep.core.model.dto.subject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 이슈 분류 (관리 > 조회)
 */
@Schema(description = "이슈 분류(관리 > 조회)")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueCategoryBasicDto {

	@Positive
	@Schema(description = "이슈 카테고리 아이디")
	private Long id;

	@Schema(description = "이슈 카테고리명")
	private String name;

//	@Positive
//	private Long branchId;

	@Positive
	@Schema(description = "이슈 카테고리 상위 분류 아이디")
	private Long parentId;

	@Positive
	@Schema(description = "이슈 카테고리 뎁스")
	private Integer depth;

	@Schema(description = "사용여부")
	private Boolean enabled;

	@Schema(description = "")
	private Boolean exposed;

	/**
	 * FIXME :: bnk 코드. 이슈 카테고리 비즈니스에서 사용됨. 제거 필요 20240714 volka
	 */
	@JsonIgnore
	@Schema(description = "bnk 코드")
	private String bnkCode;

	public static class IssueCategoryBasicDtoBuilder {}
}
