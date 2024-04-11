package com.kep.portal.model.dto.platform;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.channel.ChannelDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * 플랫폼 템플릿 목록 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformTemplateResponseDto {

	private Long id;

	private String name;

	private PlatformTemplateStatus status;

	private PlatformType platform;

	@JsonIncludeProperties({"id","name", "service_id", "service_key"})
	private ChannelDto channelInfo;

	private KakaoBizMessageTemplatePayload detail;

	private Long branchId;

	private ZonedDateTime created;

	@JsonIncludeProperties({"id","username","nickname","teams"})
	private MemberDto creatorInfo;

	private ZonedDateTime modified;

	@JsonIncludeProperties({"id","username","nickname","teams"})
	private MemberDto modifierInfo;

	private Boolean rejectYn;

	private String rejectComment;
}
