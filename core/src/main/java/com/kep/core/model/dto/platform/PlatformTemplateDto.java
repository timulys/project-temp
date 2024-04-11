package com.kep.core.model.dto.platform;

import lombok.*;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * 플랫폼 템플릿, 검색 대상이 아닌 데이터는 Payload 에 포함 (템플릿 타입, 보안 템플릿 여부 등)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformTemplateDto {

	private Long id;

	private String name;

	private String code;

	private PlatformTemplateStatus status;

	private BizTalkMessageType messageType;

	private String senderProfileKey;

	private String payload;

	private PlatformType platform;

	private Long branchId;

	private ZonedDateTime created;

	private Long creator;

	private ZonedDateTime modified;

	private Long modifier;

	private List<Long> ids;
}
