package com.kep.portal.model.dto.privilege;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

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

	private Long id;

	@NotEmpty
	@Pattern(regexp = "[A-Z0-9_]+")
	private String type;

	@NotEmpty
	private String name;

	@NotNull
	@Positive
	private LevelDto level;
}
