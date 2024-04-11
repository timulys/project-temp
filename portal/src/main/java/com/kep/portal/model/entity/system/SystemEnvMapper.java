package com.kep.portal.model.entity.system;

import com.kep.core.model.dto.system.SystemEnvDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SystemEnvMapper {

    SystemEnv map(SystemEnvDto dto);
	SystemEnvDto map(SystemEnv entity);
	List<SystemEnvDto> map(List<SystemEnv> entities);

}
