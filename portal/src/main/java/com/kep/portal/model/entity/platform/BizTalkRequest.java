package com.kep.portal.model.entity.platform;

import com.kep.core.model.dto.platform.BizTalkRequestStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.converter.ListOfLongConverter;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * 카카오 비즈톡, 요청
 */
@Entity
@Table(indexes = {
		@Index(name = "IDX_BIZTALK_REQUEST__SEARCH", columnList = "created, platform, status, branchId, teamId, creator"),
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BizTalkRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("플랫폼 타입")
	@Enumerated(EnumType.STRING)
	@NotNull
	private PlatformType platform;

	@Comment("상태")
	@Enumerated(EnumType.STRING)
	@NotNull
	private BizTalkRequestStatus status;

	@Comment("플랫폼 템플릿 PK")
	@Positive
	private Long templateId;

	@Comment("친구톡용 payload")
	@Lob
	@Basic
	@Nationalized
	private String friendPayload;

	@Comment("반려 사유")
	@Size(max = 200)
	private String reasonReject;

	@Comment("채널 PK")
	@NotNull
	@Positive
	private Long channelId;

	@Comment("브랜치 PK")
	@NotNull
	@Positive
	private Long branchId;

	@Comment("팀 PK")
	@NotNull
	@Positive
	private Long teamId;


	@Comment("예약 여부 및 예약일시")
	private ZonedDateTime reserved;

	@Comment("식별 고객 목록")
	@Convert(converter = ListOfLongConverter.class)
	private List<Long> customers;

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

	@PrePersist
	public void prePersist() {
		if (created == null) {
			created = ZonedDateTime.now();
		}
	}

	@PreUpdate
	public void preUpdate() {
		if (modified == null) {
			modified = ZonedDateTime.now();
		}
	}
}