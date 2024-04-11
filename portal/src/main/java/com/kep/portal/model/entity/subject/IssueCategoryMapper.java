package com.kep.portal.model.entity.subject;

import com.kep.core.model.dto.subject.IssueCategoryDto;
import com.kep.core.model.dto.subject.IssueCategoryBasicDto;
import com.kep.portal.model.dto.subject.IssueCategoryChildrenDto;
import com.kep.portal.model.dto.subject.IssueCategoryStoreDto;
import com.kep.portal.model.dto.subject.IssueCategoryWithChannelDto;
import com.kep.portal.model.entity.channel.Channel;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IssueCategoryMapper {

//	Logger log = LoggerFactory.getLogger(IssueCategoryMapper.class);

	IssueCategory map(IssueCategoryDto dto);

	IssueCategoryDto map(IssueCategory entity); //

	List<IssueCategoryDto> map(List<IssueCategory> entities);

	@AfterMapping
	default void setPath(@MappingTarget IssueCategoryDto.IssueCategoryDtoBuilder dto, IssueCategory entity) {

		List<IssueCategory> path = IssueCategory.getPath(entity);
		dto.path(mapBasic(path));
	}

	// ////////////////////////////////////////////////////////////////////////
	// IssueCategoryStoreDto
	IssueCategory mapStore(IssueCategoryStoreDto dto);

	// ////////////////////////////////////////////////////////////////////////
	// IssueCategoryBasicDto
	IssueCategoryBasicDto mapBasic(IssueCategory entity);
	List<IssueCategoryBasicDto> mapBasic(List<IssueCategory> entities);

	@AfterMapping
	default void setParent(@MappingTarget IssueCategoryBasicDto.IssueCategoryBasicDtoBuilder dto, IssueCategory entity) {

		if (entity.getParent() != null) {
			dto.parentId(entity.getParent().getId());
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	// IssueCategoryChildrenDto
	IssueCategoryChildrenDto mapChildren(IssueCategory entity);
	default List<IssueCategoryChildrenDto> mapChildren(List<IssueCategory> entities) {

		if (entities == null) return null;

		List<IssueCategoryChildrenDto> dtos = new ArrayList<>();
		for (IssueCategory issueCategory : entities) {
			dtos.add(mapChildren(issueCategory));
		}

		// 부모(parent)를 찾아 children 리스트에 자신(self)을 넣음
		for (IssueCategoryChildrenDto self : dtos) {
			if (self.getParentId() != null) {
				for (IssueCategoryChildrenDto parent : dtos) {
					if (self.getParentId().equals(parent.getId())) {
						if (parent.getChildren() == null) parent.setChildren(new ArrayList<>());
						parent.getChildren().add(self);
						break;
					}
				}
			}
		}

		// 대분류만 필터링
		List<IssueCategoryChildrenDto> topCategories = dtos.stream().filter(o -> o.getDepth() == 1).collect(Collectors.toList());
		return sort(topCategories);
	}

	default List<IssueCategoryChildrenDto> sort(List<IssueCategoryChildrenDto> issueCategoryChildrenDtos) {

		issueCategoryChildrenDtos.sort(Comparator.comparingInt(IssueCategoryChildrenDto::getSort));
		for (IssueCategoryChildrenDto current : issueCategoryChildrenDtos) {
			if (!ObjectUtils.isEmpty(current.getChildren())) {
				sort(current.getChildren());
			}
		}

		return issueCategoryChildrenDtos;
	}

	@AfterMapping
	default void setParent(@MappingTarget IssueCategoryChildrenDto.IssueCategoryChildrenDtoBuilder dto, IssueCategory entity) {

		if (entity.getParent() != null) {
			dto.parentId(entity.getParent().getId());
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	// IssueCategoryWithChannelDto
	IssueCategoryWithChannelDto mapWithChannel(IssueCategory entity);
	default List<IssueCategoryWithChannelDto> mapWithChannel(List<IssueCategory> entities, List<Channel> channels) {

		List<IssueCategoryWithChannelDto> dtos = new ArrayList<>();

		for (IssueCategory entity : entities) {
			IssueCategoryWithChannelDto dto = mapWithChannel(entity);
			dtos.add(dto);
			for (Channel channel : channels) {
				if (entity.getChannelId().equals(channel.getId())) {
					dto.setChannelName(channel.getName());
					break;
				}
			}
		}

		return dtos;
	}

}
