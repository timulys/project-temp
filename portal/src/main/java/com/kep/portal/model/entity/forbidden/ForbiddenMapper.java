package com.kep.portal.model.entity.forbidden;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.kep.portal.model.dto.forbidden.ForbiddenDto;

@Mapper(componentModel = "spring"
, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ForbiddenMapper {
	ForbiddenDto map(Forbidden entity);
	Forbidden map(ForbiddenDto entityDto);
	List<ForbiddenDto> map(List<Forbidden> entities);
}
