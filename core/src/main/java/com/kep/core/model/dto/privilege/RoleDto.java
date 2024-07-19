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

@Schema(description = "역할정보")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "type" })
@Builder
public class RoleDto {
	@Schema(description = "역할 아이디")
	private Long id;

	@NotEmpty
	@Pattern(regexp = "[A-Z0-9_]+")
	@Schema(description = "역할 유형")
	private String type;

	@NotEmpty
	@Schema(description = "역할명")
	private String name;

	@NotNull
	@Positive
	@Schema(description = "레벨 아이디")
	private Long levelId;

	@Schema(description = "사용여부")
	private Boolean enabled;

	@Schema(description = "")
	private List<PrivilegeDto> privileges;
}
