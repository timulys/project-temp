package com.kep.portal.model.entity.privilege;

import com.kep.portal.model.dto.privilege.LevelDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LevelMapper {

	Level map(LevelDto dto);

	LevelDto map(Level entity);

	List<LevelDto> map(List<Level> entities);
}
