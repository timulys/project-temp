package com.kep.portal.model.entity.platform;

import com.kep.core.model.dto.platform.PlatformTemplateDto;
import com.kep.portal.model.dto.platform.PlatformTemplateResponseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlatformTemplateMapper {

	PlatformTemplate map(PlatformTemplateDto dto);

	PlatformTemplateDto map(PlatformTemplate entity);
	
	PlatformTemplate map(PlatformTemplateResponseDto dto);

	List<PlatformTemplateDto> map(List<PlatformTemplate> entities);

	PlatformTemplateResponseDto mapResponse(PlatformTemplate entity);

	List<PlatformTemplateResponseDto> mapResponseList(List<PlatformTemplate> entity);

	@AfterMapping
	default void toAfter(PlatformTemplate entity, @MappingTarget PlatformTemplateDto.PlatformTemplateDtoBuilder dto) {

	}
}
