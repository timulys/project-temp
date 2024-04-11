package com.kep.portal.model.entity.team;

import com.kep.core.model.dto.team.TeamDto;
import com.kep.portal.model.entity.member.MemberMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
        , uses = {MemberMapper.class}
        , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeamMapper {

    Team map(TeamDto dto);
    TeamDto map(Team entity);
    List<TeamDto> map(List<Team> entities);


    @AfterMapping
    default void teamEntityToTeamDto(Team entity , @MappingTarget TeamDto.TeamDtoBuilder dto){
//        if(entity.getTeamOfficeHours() != null){
//            Map<String,String> startCounselTime = OfficeHoursTimeUtils.hours(entity.getTeamOfficeHours().getStartCounselTime());
//            Map<String,String> endCounselTime = OfficeHoursTimeUtils.hours(entity.getTeamOfficeHours().getEndCounselTime());
//            List<String> dayOfWeek = OfficeHoursTimeUtils.dayOfWeek(startCounselTime.get("dayOfWeek"));
//
//            dto.officeHours(OfficeHoursDto.builder()
//                    .id(entity.getTeamOfficeHours().getId())
//                    .dayOfWeek(dayOfWeek)
//                    .startCounselHours(startCounselTime.get("hours"))
//                    .startCounselMinutes(startCounselTime.get("minutes"))
//                    .endCounselHours(endCounselTime.get("hours"))
//                    .endCounselMinutes(endCounselTime.get("minutes"))
//                    .build());
//        }

    }
}
