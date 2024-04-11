package com.kep.portal.model.entity.guide;

import com.kep.core.model.dto.guide.GuideLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
        , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GuideLogMapper {
    GuideLog map(GuideLogDto dto);

    GuideLogDto map(GuideLog entity);

    List<GuideLogDto> map(List<GuideLog> entities);
}
