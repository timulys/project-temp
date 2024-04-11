package com.kep.portal.model.entity.privilege;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.util.ObjectUtils;

import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.portal.model.dto.privilege.RoleWithLevelDto;

@Mapper(componentModel = "spring", uses = { LevelMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

	Role map(RoleDto dto);

	RoleDto map(Role entity);

	List<RoleDto> map(List<Role> entities);

	@AfterMapping
	default void setLevelId(@MappingTarget RoleDto.RoleDtoBuilder dto, Role entity) {

		if (!ObjectUtils.isEmpty(entity.getLevel())) {
			dto.levelId(entity.getLevel().getId());
		}
	}

	RoleWithLevelDto mapWithLevel(Role entity);

	List<RoleWithLevelDto> mapWithLevel(List<Role> entities);
}
