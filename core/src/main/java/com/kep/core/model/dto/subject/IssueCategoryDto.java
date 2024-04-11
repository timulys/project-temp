package com.kep.core.model.dto.subject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 이슈 분류
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueCategoryDto {

	@Positive
	private Long id;

	private String name;

	@Positive
	private Long branchId;

	@JsonIgnoreProperties({"path"})
	private IssueCategoryBasicDto parent;

	@Positive
	private Integer depth;

	private Boolean exposed;

	private String bnkCode;

	@JsonIgnoreProperties({"parent", "path"})
	private List<IssueCategoryBasicDto> path;
	
	public static class IssueCategoryDtoBuilder {}
}
