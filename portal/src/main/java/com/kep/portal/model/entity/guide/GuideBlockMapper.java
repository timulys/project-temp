package com.kep.portal.model.entity.guide;

import com.kep.core.model.dto.guide.GuideBlockDto;
import com.kep.core.model.dto.guide.GuideDto;
import com.kep.portal.model.entity.branch.BranchMapper;
import com.kep.portal.model.entity.guide.GuideBlock;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.team.TeamMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
        , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GuideBlockMapper {
    GuideBlock map(GuideBlockDto dto);

    @Mapping(target = "messageCondition", ignore = true)
    @Mapping(target = "fileCondition", ignore = true)
    GuideBlockDto map(GuideBlock entity);

    List<GuideBlockDto> map(List<GuideBlock> entities);

}
