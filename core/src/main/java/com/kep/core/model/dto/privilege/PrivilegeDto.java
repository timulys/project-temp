package com.kep.core.model.dto.privilege;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"type"})
public class PrivilegeDto {

	private Long id;

	@NotEmpty
	@Pattern(regexp = "[A-Z0-9_]+")
	private String type;

	@NotEmpty
	private String name;

	@NotEmpty
	private String topMenu;

	@NotEmpty
	private String midMenu;
}
