package com.kep.portal.model.entity.work;


import com.kep.core.model.dto.work.OfficeHoursDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.team.TeamMapper;
import com.kep.portal.util.OfficeHoursTimeUtils;
import org.mapstruct.*;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring"
        , unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface OfficeHoursMapper {

    @Mapping(target = "dayOfWeek", ignore = true)
    OfficeHoursDto map(OfficeHours entity);
    @Mapping(target = "dayOfWeek", ignore = true)
    OfficeHours map(OfficeHoursDto dto);
    @Mapping(target = "dayOfWeek", ignore = true)
    List<OfficeHoursDto> map(List<OfficeHours> entity);



//    @BeforeMapping
//    default void toBefore(OfficeHoursDto dto , @MappingTarget OfficeHours.OfficeHoursBuilder entity){
//        String startHours   = OfficeHoursTimeUtils.hours(dto.getStartCounselHours(),dto.getStartCounselMinutes());
//        String endHours     = OfficeHoursTimeUtils.hours(dto.getEndCounselHours() , dto.getEndCounselMinutes());
//        String dayOfWeek    = OfficeHoursTimeUtils.dayOfWeek(dto.getDayOfWeek());
//
//        entity.startCounselTime(startHours);
//        entity.endCounselTime(endHours);
//        entity.dayOfWeek(dayOfWeek).build();
//    }

    @AfterMapping
    default void toAfter(OfficeHours entity , @MappingTarget OfficeHoursDto.OfficeHoursDtoBuilder dto) {

        Map<String,String> startHours   = OfficeHoursTimeUtils.hours(entity.getStartCounselTime());
        Map<String,String> endHours     = OfficeHoursTimeUtils.hours(entity.getEndCounselTime());
        List<String> dayOfWeek          = OfficeHoursTimeUtils.dayOfWeek(entity.getDayOfWeek());

        if(WorkType.Cases.branch.equals(entity.getCases())){
            dto.branchId(entity.getCasesId());
        }

        if(WorkType.Cases.member.equals(entity.getCases())){
            dto.memberId(entity.getCasesId());
        }


        dto.dayOfWeek(dayOfWeek);
        dto.startCounselHours(startHours.get("hours"));
        dto.startCounselMinutes(startHours.get("minutes"));
        dto.endCounselHours(endHours.get("hours"));
        dto.endCounselMinutes(endHours.get("minutes"));
        dto.cases(null).build();
    }
}
