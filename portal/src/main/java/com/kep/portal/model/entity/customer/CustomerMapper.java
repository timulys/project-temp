package com.kep.portal.model.entity.customer;

import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.portal.model.dto.customer.request.PatchCustomerRequestDto;
import com.kep.portal.model.entity.issue.IssueExtraMapper;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring"
		, uses = {CustomerContactMapper.class, CustomerAnniversaryMapper.class , CustomerAuthorizedMapper.class , IssueExtraMapper.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

	Customer map(CustomerDto dto);
	Customer map(PatchCustomerRequestDto dto);
	CustomerDto map(Customer entity);

	List<CustomerDto> map(List<Customer> entities);
}
