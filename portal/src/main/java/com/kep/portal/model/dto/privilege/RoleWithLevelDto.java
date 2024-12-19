package com.kep.portal.model.dto.privilege;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "type" })
@Builder
public class RoleWithLevelDto {

	@Schema(description = "Role(역할) 아이디 (PK)")
	private Long id;

	@Schema(description = "역할 타입")
	@NotEmpty
	@Pattern(regexp = "[A-Z0-9_]+")
	private String type;

	@Schema(description = "역할 이름")
	@NotEmpty
	private String name;

	@Schema(description = "역할 별 level")
	@NotNull
	@Positive
	private LevelDto level;
}
