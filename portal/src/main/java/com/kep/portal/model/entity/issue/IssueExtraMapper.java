package com.kep.portal.model.entity.issue;

import com.kep.core.model.dto.issue.IssueExtraDto;
import com.kep.portal.model.entity.subject.IssueCategoryMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring"
		, uses = {IssueCategoryMapper.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IssueExtraMapper {

	IssueExtra map(IssueExtraDto dto);
	IssueExtraDto map(IssueExtra entity);
	List<IssueExtraDto> map(List<IssueExtra> entities);

	IssueExtraDto mapMemo(IssueExtraMemo entity);
	List<IssueExtraDto> mapMemo(List<IssueExtraMemo> entities);

	@AfterMapping
	default void toAfter(IssueExtra entity, @MappingTarget IssueExtraDto.IssueExtraDtoBuilder dto) {

	}
}
