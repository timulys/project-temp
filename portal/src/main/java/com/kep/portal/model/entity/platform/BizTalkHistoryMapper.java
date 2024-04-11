package com.kep.portal.model.entity.platform;

import com.kep.core.model.dto.platform.BizTalkHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
        , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BizTalkHistoryMapper {

    @Mapping(target = "creator", ignore = true)
    BizTalkHistory map(BizTalkHistoryDto dto);

    @Mapping(target = "creator", ignore = true)
    BizTalkHistoryDto map(BizTalkHistory entity);

    List<BizTalkHistoryDto> map(List<BizTalkHistory> entities);
}
