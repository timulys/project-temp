package com.kep.portal.model.dto.privilege;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LevelDto {

	@Schema(description = "레벨 ID ( 상담원 : 1 , 매니저 : 2, 관리자 : 3, 마스터 : 4 )")
	@Positive
	private Long id;


	@Schema(description = "레벨 타입")
	@NotEmpty
	@Pattern(regexp = "[A-Z0-9_]+")
	private String type;

	@Schema(description = "레벨 이름")
	@NotEmpty
	private String name;
}
