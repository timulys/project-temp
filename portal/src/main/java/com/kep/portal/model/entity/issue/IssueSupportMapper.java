package com.kep.portal.model.entity.issue;

import com.kep.core.model.dto.issue.IssueSupportDto;
import com.kep.core.model.dto.issue.IssueSupportHistoryDto;
import com.kep.portal.model.dto.issue.IssueSupportDetailDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IssueSupportMapper {

	IssueSupport map(IssueSupportDto dto);
	IssueSupportDto map(IssueSupport entity);
	List<IssueSupportDto> map(List<IssueSupport> entities);

	IssueSupportDetailDto mapDetail(IssueSupport dto);


	IssueSupportHistoryDto mapHistory(IssueSupportHistory entity);

	@AfterMapping
	default void toAfter(IssueSupport entity, @MappingTarget IssueSupportDto.IssueSupportDtoBuilder dto) {

	}
}
