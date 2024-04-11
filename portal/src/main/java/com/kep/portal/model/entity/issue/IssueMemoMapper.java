 /**
  * issue_memo mapper 
  * 
  *  @생성일자      / 만든사람		 / 수정내용
  *  2023.04.04  / philip.lee7   / 신규
  */
package com.kep.portal.model.entity.issue;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.kep.core.model.dto.issue.IssueMemoDto;


@Mapper(componentModel = "spring"
, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IssueMemoMapper {

	IssueMemoDto map(IssueMemo entity);

	IssueMemo map(IssueMemoDto dto);
	
	List<IssueMemoDto> map(List<IssueMemo> entities);
}
