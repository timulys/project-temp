package com.kep.core.model.dto.subject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 이슈 분류 (관리 > 조회)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueCategoryBasicDto {

	@Positive
	private Long id;

	private String name;

//	@Positive
//	private Long branchId;

	@Positive
	private Long parentId;

	@Positive
	private Integer depth;

	private Boolean enabled;

	private Boolean exposed;
	
	@JsonIgnore
	private String bnkCode;

	public static class IssueCategoryBasicDtoBuilder {}
}
