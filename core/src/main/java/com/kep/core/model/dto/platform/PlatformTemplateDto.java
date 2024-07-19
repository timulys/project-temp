package com.kep.core.model.dto.platform;

import io.swagger.v3.oas.annotations.media.Schema;
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

	@Schema(description = "플랫폼 템플릿 아이디")
	private Long id;

	@Schema(description = "플랫폼 템플릿명")
	private String name;

	/**
	 * FIXME :: 찾아야함 20240715 volka
	 */
	@Schema(description = "코드")
	private String code;

	@Schema(description = "플랫폼 템플릿 상태")
	private PlatformTemplateStatus status;

	@Schema(description = "request, // 검수요청\n" +
			"approve, // 승인\n" +
			"reject, // 반려\n" +
			"temp, // 임시저장\n" +
			"delete // 삭제")
	private BizTalkMessageType messageType;

	@Schema(description = "발신 프로필 키")
	private String senderProfileKey;

	@Schema(description = "내용")
	private String payload;

	@Schema(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template,\n" +
			"legacy_web, legacy_app , kakao_counsel_center)")
	private PlatformType platform;

	@Schema(description = "브랜치 아이디")
	private Long branchId;

	@Schema(description = "생성일시")
	private ZonedDateTime created;

	@Schema(description = "생성자")
	private Long creator;

	@Schema(description = "수정일시")
	private ZonedDateTime modified;

	@Schema(description = "수정자")
	private Long modifier;

	@Schema(description = "플랫폼 템플릿 아이디 목록")
	private List<Long> ids;
}
