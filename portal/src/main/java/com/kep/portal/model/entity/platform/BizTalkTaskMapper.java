package com.kep.portal.model.entity.platform;

import com.kep.core.model.dto.platform.BizTalkTaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BizTalkTaskMapper {

    @Mapping(target = "creator", ignore = true)
    BizTalkTask map(BizTalkTaskDto dto);

    @Mapping(target = "creator", ignore = true)
    BizTalkTaskDto map(BizTalkTask entity);

    List<BizTalkTaskDto> map(List<BizTalkTask> entities);
}
