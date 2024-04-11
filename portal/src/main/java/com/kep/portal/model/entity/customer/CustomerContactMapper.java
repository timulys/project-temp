package com.kep.portal.model.entity.customer;

import com.kep.core.model.dto.customer.CustomerContactDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring"
//		, uses = {CustomerTranslator.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerContactMapper {

	CustomerContact map(CustomerContactDto dto);

//	@Mapping( target = "payload", qualifiedByName = { "CustomerTranslator", "Decrypt" } )
	CustomerContactDto map(CustomerContact entity);

	List<CustomerContactDto> map(List<CustomerContact> entities);

	List<CustomerContact> mapDto(List<CustomerContactDto> dtos);
}
