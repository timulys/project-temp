package com.kep.portal.model.dto.assign;

import com.kep.portal.model.entity.assign.AssignMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * DTO of {@link AssignMethod}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignMethodDto {

	@NotNull
	private Long id;

	@NotEmpty
	private String name;

	@NotNull
	private Integer sort;

	@NotNull
	private Boolean enabled;
}
