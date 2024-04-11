package com.kep.portal.model.entity.guide;

import com.kep.core.model.dto.guide.GuideCategoryDto;
import com.kep.core.model.dto.guide.GuideDto;
import com.kep.portal.model.entity.branch.BranchMapper;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.team.TeamMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring"
        , uses = {BranchMapper.class, TeamMapper.class, MemberMapper.class, GuideBlockMapper.class}
        , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GuideMapper {
    Guide map(GuideDto dto);

    GuideDto map(Guide entity);

    List<GuideDto> map(List<Guide> entities);

    @AfterMapping
    default void setPath(@MappingTarget GuideDto.GuideDtoBuilder dto, Guide entity) {

        List<GuideCategoryDto> path = new ArrayList<>();
        dto.categoryList(path);

        GuideCategory current = entity.getGuideCategory();

        GuideCategoryDto parent = GuideCategoryDto.builder()
                .id(current.getId())
                .depth(current.getDepth())
                .name(current.getName())
                .build();
        path.add(parent);


        while (current.getParent() != null) {
            current = current.getParent();
            GuideCategoryDto temp = GuideCategoryDto.builder()
                    .id(current.getId())
                    .depth(current.getDepth())
                    .name(current.getName())
                    .build();
            path.add(temp);
        }

        path.sort(Comparator.comparingInt(GuideCategoryDto::getDepth));
    }

}
