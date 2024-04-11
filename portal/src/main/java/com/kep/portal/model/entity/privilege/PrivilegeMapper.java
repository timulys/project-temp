package com.kep.portal.model.entity.privilege;

import com.kep.core.model.dto.privilege.PrivilegeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrivilegeMapper {

	Privilege map(PrivilegeDto dto);

	PrivilegeDto map(Privilege entity);

	List<PrivilegeDto> map(List<Privilege> entities);
}
