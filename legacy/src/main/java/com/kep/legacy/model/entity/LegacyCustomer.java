package com.kep.legacy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LegacyCustomer {

	@Positive
	private Long id;

	@NotEmpty
	private String name;

	private String ci;
}
