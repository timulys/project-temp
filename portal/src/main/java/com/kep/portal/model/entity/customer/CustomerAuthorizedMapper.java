package com.kep.portal.model.entity.customer;

import com.kep.core.model.dto.customer.CustomerAnniversaryDto;
import com.kep.core.model.dto.customer.CustomerAuthorizedDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerAuthorizedMapper {
	CustomerAuthorized map(CustomerAuthorizedDto dto);
	CustomerAuthorizedDto map(CustomerAuthorized entity);
	List<CustomerAuthorizedDto> map(List<CustomerAuthorized> entities);
}
