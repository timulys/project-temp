package com.kep.portal.model.entity.site;

import com.kep.portal.model.dto.site.MenuDto;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper {

	@Mapping(target = "disabledLevels", ignore = true)
	MenuDto map(Menu entity);

	@Mapping(target = "disabledLevels", ignore = true)
	List<MenuDto> map(List<Menu> entities);

	@AfterMapping
	default void setDisabledRoles(@MappingTarget MenuDto.MenuDtoBuilder dto, Menu entity) {

		if (!ObjectUtils.isEmpty(entity.getDisabledLevels())) {
			dto.disabledLevels(Arrays.asList(entity.getDisabledLevels().split(","))
					.stream().map(String::trim).collect(Collectors.toSet()));
		}
	}
}
