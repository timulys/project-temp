package com.kep.portal.model.entity.platform;

import com.kep.core.model.dto.platform.BizTalkMessageType;
import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.PlatformType;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

/**
 * 플랫폼 템플릿, 검색 대상이 아닌 데이터는 Payload 에 포함 (템플릿 타입, 보안 템플릿 여부 등)
 */
@Entity
@Table(indexes = {
			@Index(name = "IDX_PLATFORM_TEMPLATE__SEARCH", columnList = "platform, modified"),
			@Index(name = "IDX_PLATFORM_TEMPLATE__NAME", columnList = "platform, name, modified"),
			@Index(name = "IDX_PLATFORM_TEMPLATE__STATUS", columnList = "status")
		},
		uniqueConstraints = {
				@UniqueConstraint(name = "UK_PLATFORM_TEMPLATE__CODE", columnNames = { "code" })
		})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlatformTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("이름")
	@NotNull
	private String name;

	@Comment("템플릿코드")
	private String code;

	@Comment("검수상태")
	@Enumerated(EnumType.STRING)
	@NotNull
	private PlatformTemplateStatus status;

	@Comment("메세지 타입")
	@Enumerated(EnumType.STRING)
	private BizTalkMessageType messageType;

	@Comment("채널ID")
	private Long channelId;

	@Comment("발신프로필 키")
	private String senderProfileKey;

	@Column(length = 4000)
	@Comment("템플릿 포맷")
//	@NotNull
	private String payload;

	@Comment("플랫폼 타입")
	@Enumerated(EnumType.STRING)
	@NotNull
	private PlatformType platform;

	@Comment("브랜치 PK")
	@NotNull
	@Positive
	private Long branchId;

	@Comment("생성 일시")
	@NotNull
	private ZonedDateTime created;

	@Comment("생성자")
	@NotNull
	@Positive
	private Long creator;

	@Comment("수정 일시")
	private ZonedDateTime modified;

	@Comment("수정자")
	@Positive
	private Long modifier;
}
