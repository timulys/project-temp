package com.kep.portal.model.entity.customer;

import com.kep.core.model.dto.customer.CustomerContactDto;
import com.kep.core.model.dto.customer.CustomerMemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring"
		, uses = {CustomerMapper.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMemberMapper {

	CustomerMember map(CustomerMemberDto dto);
	CustomerMemberDto map(CustomerMember entity);
	List<CustomerMemberDto> map(List<CustomerMember> entities);
}
