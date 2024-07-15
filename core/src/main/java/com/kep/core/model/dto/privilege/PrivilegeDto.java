package com.kep.core.model.dto.privilege;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"type"})
public class PrivilegeDto {

	@Schema(description = "")
	private Long id;

	@NotEmpty
	@Pattern(regexp = "[A-Z0-9_]+")
	@Schema(description = "")
	private String type;

	@NotEmpty
	@Schema(description = "")
	private String name;

	@NotEmpty
	@Schema(description = "")
	private String topMenu;

	@NotEmpty
	@Schema(description = "")
	private String midMenu;
}
