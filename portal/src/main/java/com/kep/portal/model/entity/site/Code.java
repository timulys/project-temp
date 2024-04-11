package com.kep.portal.model.entity.site;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Code {

	@NotNull
	private String group;

	@NotNull
	private String code;

	@NotNull
	private String name;

	@NotNull
	private Boolean enabled;

	@NotNull
	@Positive
	private Integer sort;
}
