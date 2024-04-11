package com.kep.portal.model.entity.system;

import com.kep.core.model.dto.system.SystemEnvDto;
import com.kep.core.model.dto.system.SystemIssuePayloadDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SystemIssuePayloadMapper {

    SystemIssuePayload map(SystemIssuePayloadDto dto);
	SystemIssuePayloadDto map(SystemIssuePayload entity);
	List<SystemIssuePayloadDto> map(List<SystemIssuePayload> entities);

}
