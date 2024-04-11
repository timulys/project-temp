package com.kep.portal.model.entity.env;

import com.kep.core.model.dto.env.CounselEnvDto;
import com.kep.core.model.dto.env.CounselInflowEnvDto;
import com.kep.portal.model.entity.team.TeamMapper;
import com.kep.portal.model.entity.work.OfficeHoursMapper;
import com.kep.portal.util.OfficeHoursTimeUtils;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CounselInflowEnvMapper {
	
    CounselInflowEnv map(CounselInflowEnvDto dto);
	CounselInflowEnvDto map(CounselInflowEnv entity);
	List<CounselInflowEnvDto> map(List<CounselInflowEnv> entities);

}
