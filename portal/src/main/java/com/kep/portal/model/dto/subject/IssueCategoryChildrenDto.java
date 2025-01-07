package com.kep.portal.model.dto.subject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 이슈 분류 (하위 분류 목록 포함)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class IssueCategoryChildrenDto {

	@Schema(description = "이슈 카테고리 아이디(PK)")
	@Positive
	private Long id;

	@Schema(description = "이슈 카테고리 이름")
	private String name;

	@JsonIgnore
	private Long parentId;

	@JsonIgnore
	private Integer sort;

	@JsonIgnore
	private Integer depth;

	@Schema(description = "채널 ID")
	private Long channelId;
	
	@JsonIgnore
	private String bnkCode;

	private List<IssueCategoryChildrenDto> children;

	public static class IssueCategoryChildrenDtoBuilder {}
}
