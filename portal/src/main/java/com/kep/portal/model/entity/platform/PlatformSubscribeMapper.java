package com.kep.portal.model.entity.platform;

import com.kep.core.model.dto.platform.PlatformSubscribeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlatformSubscribeMapper {

	PlatformSubscribeDto map(PlatformSubscribe entity);
	PlatformSubscribe map(PlatformSubscribeDto dto);
	List<PlatformSubscribeDto> map(List<PlatformSubscribe> entities);
}
