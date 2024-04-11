package com.kep.platform.model.entity;

import com.kep.core.model.dto.event.PlatformEventDto;
import com.kep.core.model.dto.issue.IssueDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlatformSessionMapper {

	@Mapping(target = "issueId", source = "issueDto.id")
	@Mapping(target = "created", source = "issueDto.created")
	@Mapping(target = "id", ignore = true)
	PlatformSession map(PlatformEventDto platformEventDto, IssueDto issueDto);

	@Mapping(target = "id", ignore = true)
	PlatformSession map(PlatformEventDto platformEventDto);

	@AfterMapping
	default void setId(@MappingTarget PlatformSession.PlatformSessionBuilder platformSession, PlatformEventDto platformEventDto) {

		platformSession.id(PlatformSession.buildId(platformEventDto.getPlatformType(),
				platformEventDto.getServiceKey(), platformEventDto.getUserKey()));
	}
}
