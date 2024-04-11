package com.kep.core.model.dto.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kep.core.model.dto.issue.IssueExtraDto;
import com.kep.core.model.dto.legacy.LegacyCustomerDto;
import com.kep.core.model.dto.platform.PlatformSubscribeDto;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerDto {

	@Positive
	private Long id;
	@JsonIgnore
	private String identifier;
	private String name;

	@URL
	private String profile;

	private String age;

	private List<CustomerContactDto> contacts;

	private List<CustomerAnniversaryDto> anniversaries;

	private List<CustomerAuthorizedDto> authorizeds;

	private List<PlatformSubscribeDto> platformSubscribes;

	private List<IssueExtraDto> inflows;

	private LegacyCustomerDto legacyCustomerData;
}
