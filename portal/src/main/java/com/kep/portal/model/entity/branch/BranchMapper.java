package com.kep.portal.model.entity.branch;

import com.kep.core.model.dto.branch.BranchDto;
import com.kep.portal.model.entity.team.TeamMapper;
import com.kep.portal.model.entity.work.OfficeHoursMapper;
import com.kep.portal.util.OfficeHoursTimeUtils;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring"
		, uses = {OfficeHoursMapper.class, TeamMapper.class , OfficeHoursTimeUtils.class , BranchRoleMapper.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BranchMapper {

	Branch map(BranchDto dto);

	BranchDto map(Branch entity);

	List<BranchDto> map(List<Branch> entities);
}
