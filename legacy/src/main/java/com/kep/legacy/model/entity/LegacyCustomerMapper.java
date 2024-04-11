package com.kep.legacy.model.entity;

import com.kep.core.model.dto.legacy.LegacyCustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LegacyCustomerMapper {

	LegacyCustomerDto map(LegacyCustomer entity);

	List<LegacyCustomerDto> map(List<LegacyCustomer> entities);
}
