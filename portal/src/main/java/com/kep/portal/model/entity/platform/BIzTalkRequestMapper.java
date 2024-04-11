package com.kep.portal.model.entity.platform;

import com.kep.core.model.dto.platform.BizTalkRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
        ,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BIzTalkRequestMapper {
    @Mapping(target = "customers", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "friendPayload", ignore = true)
    BizTalkRequest map(BizTalkRequestDto dto);

    @Mapping(target = "customers", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "friendPayload", ignore = true)
    BizTalkRequestDto map(BizTalkRequest entity);

    List<BizTalkRequestDto> map(List<BizTalkRequest> entities);
}
