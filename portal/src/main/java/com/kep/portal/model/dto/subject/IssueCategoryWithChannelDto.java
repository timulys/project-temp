package com.kep.portal.model.dto.subject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

/**
 * 이슈 분류 (채널 정보 포함)
 * 상담 관리 > 상담 이력 > 검색 조건
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueCategoryWithChannelDto {

	@Positive
	private Long id;

	private String name;

	@Positive
	private Long parentId;

	private String channelName;

//	@Positive
//	private Integer depth;
//
//	private Boolean enabled;
//
//	private Boolean exposed;

	public static class IssueCategoryWithChannelDtoBuilder {}
}
