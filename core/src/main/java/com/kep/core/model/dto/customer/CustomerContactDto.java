package com.kep.core.model.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerContactDto {

	private Long id;

	private CustomerContactType type;

	private CustomerDto customer;

	@Size(max = 1000)
	private String payload;
}
