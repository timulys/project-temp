package com.kep.portal.model.entity.env;

import com.kep.core.model.dto.env.CounselEnvDto;
import com.kep.portal.model.entity.system.SystemEnvMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring"
		, uses = {CounselInflowEnvMapper.class, SystemEnvMapper.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CounselEnvMapper {

    CounselEnv map(CounselEnvDto dto);
	CounselEnvDto map(CounselEnv entity);
	List<CounselEnvDto> map(List<CounselEnv> entities);

}
