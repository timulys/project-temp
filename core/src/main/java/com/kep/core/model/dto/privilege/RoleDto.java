package com.kep.core.model.dto.privilege;

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

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "type" })
@Builder
public class RoleDto {
	private Long id;

	@NotEmpty
	@Pattern(regexp = "[A-Z0-9_]+")
	private String type;

	@NotEmpty
	private String name;

	@NotNull
	@Positive
	private Long levelId;

	private Boolean enabled;

	private List<PrivilegeDto> privileges;
}
