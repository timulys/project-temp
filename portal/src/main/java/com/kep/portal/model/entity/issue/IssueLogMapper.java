package com.kep.portal.model.entity.issue;

import com.kep.core.model.dto.issue.IssueLogDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IssueLogMapper {

	IssueLog map(IssueLogDto dto);

	IssueLogDto map(IssueLog entity);

	List<IssueLogDto> map(List<IssueLog> entities);
}
