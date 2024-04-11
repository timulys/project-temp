package com.kep.portal.model.entity.issue;

import com.kep.core.model.dto.issue.IssueExtraDto;
import com.kep.core.model.dto.issue.IssueHighlightDto;
import com.kep.portal.model.entity.subject.IssueCategoryMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, uses = {IssueCategoryMapper.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IssueHighlightMapper {

	IssueHighlight map(IssueHighlightDto dto);
	IssueHighlightDto map(IssueHighlight entity);
	List<IssueHighlightDto> map(List<IssueHighlight> entities);

	@AfterMapping
	default void toAfter(IssueHighlight entity, @MappingTarget IssueHighlightDto.IssueHighlightDtoBuilder dto) {

	}
}
