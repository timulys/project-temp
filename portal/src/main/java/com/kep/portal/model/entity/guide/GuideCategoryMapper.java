package com.kep.portal.model.entity.guide;

import com.kep.core.model.dto.guide.GuideCategoryDto;
import com.kep.portal.model.entity.branch.BranchMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {BranchMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GuideCategoryMapper {
    GuideCategory map(GuideCategoryDto dto);

    GuideCategoryDto map(GuideCategory entity);

    List<GuideCategoryDto> map(List<GuideCategory> entities);

    @AfterMapping
    default void setParentId(@MappingTarget GuideCategoryDto.GuideCategoryDtoBuilder dto, GuideCategory entity) {

        GuideCategory current = entity;

        if (current.getParent() != null) {
            dto.parentId(current.getParent().getId());
        }
    }

}
