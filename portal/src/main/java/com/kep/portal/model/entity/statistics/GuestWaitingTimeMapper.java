package com.kep.portal.model.entity.statistics;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.kep.portal.model.dto.statistics.GuestWaitingTimeDto;

@Mapper(componentModel = "spring"
, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GuestWaitingTimeMapper {
	List<GuestWaitingTimeDto> map(List<GuestWaitingTime> entities);
}
