package com.kep.portal.model.entity.notice;

import com.kep.core.model.dto.notice.NoticeDto;
import com.kep.portal.model.dto.notice.NoticeResponseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoticeMapper {

	Notice map(NoticeDto dto);
	NoticeDto map(Notice entity);
	
	// [2023.03.28 / philip.lee7 / noticeUpload 리스트 추가로 map 추가]
	Notice map(NoticeResponseDto dto);	
	List<NoticeDto> map(List<Notice> entities);

	NoticeResponseDto mapResponse(Notice entity);

	List<NoticeResponseDto> mapResponse(List<Notice> entities);

	@AfterMapping
	default void toAfter(Notice entity, @MappingTarget NoticeDto.NoticeDtoBuilder dto) {

	}
}
