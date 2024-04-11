package com.kep.portal.model.entity.system;

import com.kep.core.model.dto.system.SystemEnvDto;
import com.kep.core.model.dto.system.SystemEventHistoryDto;
import com.kep.portal.model.entity.customer.CustomerMapper;
import com.kep.portal.model.entity.member.MemberMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, uses = {MemberMapper.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SystemEventHistoryMapper {

    SystemEventHistory map(SystemEventHistoryDto dto);
	SystemEventHistoryDto map(SystemEventHistory entity);
	List<SystemEventHistoryDto> map(List<SystemEventHistory> entities);

}
