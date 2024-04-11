package com.kep.portal.model.entity.member;

import com.kep.portal.model.dto.member.MemberScheduleDto;
import com.kep.portal.model.entity.customer.CustomerMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.time.ZonedDateTime;
import java.util.List;

@Mapper(componentModel = "spring"
        , uses = {CustomerMapper.class, MemberMapper.class}
        , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberScheduleMapper {

    List<MemberScheduleDto> map(List<MemberSchedule> entities);

    MemberScheduleDto map(MemberSchedule entity);

    MemberSchedule map(MemberScheduleDto dto);

//    List<MemberAutoMessageTemplateDto> mapTemplates(List<MemberAutoMessageTemplate> entities);

    @AfterMapping
    default void toAfter(MemberSchedule entity , @MappingTarget MemberScheduleDto.MemberScheduleDtoBuilder dto){
        if(entity.getStartDateTime() != null && entity.getEndDateTime() != null){
            ZonedDateTime startDateTime = entity.getStartDateTime();
            ZonedDateTime endDateTime = entity.getEndDateTime();

            dto.startDate(startDateTime.toLocalDate());
            dto.startTime(startDateTime.toLocalTime());

            dto.endDate(endDateTime.toLocalDate());
            dto.endTime(endDateTime.toLocalTime());
            dto.build();
        }
    }
}
