package com.kep.portal.model.dto.privilege;

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

	@Positive
	private Long id;

	@NotEmpty
	@Pattern(regexp = "[A-Z0-9_]+")
	private String type;

	@NotEmpty
	private String name;
}
