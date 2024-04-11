package com.kep.portal.model.entity.customer;

import com.kep.core.model.dto.customer.CustomerAnniversaryDto;
import com.kep.core.model.dto.customer.CustomerContactDto;
import com.kep.core.model.dto.work.OffDutyHoursDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.work.OffDutyHours;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerAnniversaryMapper {
	CustomerAnniversary map(CustomerAnniversaryDto dto);
	CustomerAnniversaryDto map(CustomerAnniversary entity);
	List<CustomerAnniversaryDto> map(List<CustomerAnniversary> entities);


	@AfterMapping
	default void toAfter(CustomerAnniversary entity, @MappingTarget CustomerAnniversaryDto.CustomerAnniversaryDtoBuilder dto) {
		dto.anniversary(entity.getAnniversary().toString());
		dto.build();
	}
}
