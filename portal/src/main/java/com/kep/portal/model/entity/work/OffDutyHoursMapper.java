package com.kep.portal.model.entity.work;

import com.kep.core.model.dto.work.OffDutyHoursDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.util.WorkDateTimeUtils;
import org.mapstruct.*;

import java.time.ZonedDateTime;
import java.util.List;

@Mapper(componentModel = "spring"
		, uses = {WorkDateTimeUtils.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface OffDutyHoursMapper {
	OffDutyHours map(OffDutyHoursDto dto);
	OffDutyHoursDto map(OffDutyHours entity);
	List<OffDutyHoursDto> map(List<OffDutyHours> entities);

	@AfterMapping
	default void toAfter(OffDutyHours entity, @MappingTarget OffDutyHoursDto.OffDutyHoursDtoBuilder dto) {
		if(WorkType.Cases.branch.equals(entity.getCases())){
			dto.branchId(entity.getCasesId());
		}
		if(entity.getCases().equals(WorkType.Cases.member)){
			dto.memberId(entity.getCasesId());
		}
		dto.cases(null).build();
	}
}
