package com.kep.portal.model.entity.platform;

import com.kep.core.model.dto.platform.BizTalkRequestStatus;
import com.kep.core.model.dto.platform.BizTalkTaskStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.scheduler.SendBizTalkJob;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

/**
 * 카카오 비즈톡, 발송 예약
 * 비즈톡 발송 예약 스케줄러 ({@link SendBizTalkJob}) 에서 사용
 */
@Entity
@Table(indexes = {
		@Index(name = "IDX_BIZTALK_JOB__SEARCH", columnList = "created, platform, status, branchId, teamId, creator"),
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BizTalkTask {

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
	private BizTalkTaskStatus status;

	@Comment("승인 상태")
	@Enumerated(EnumType.STRING)
	@NotNull
	private BizTalkRequestStatus requestStatus;

	@Comment("플랫폼 템플릿 PK")
	@Positive
	private Long templateId;

//	@Comment("플랫폼 템플릿 이름")
//	private String templateName;

	@Comment("식별 고객")
	@NotNull
	@Positive
//	private Customer guestId; // ManyToOne
	private Long customerId;

	@Comment("요청 PK")
	@NotNull
	private Long requestId;

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

	@Comment("예약일시")
	private ZonedDateTime reserved;

	@Comment("생성 일시")
	@NotNull
	private ZonedDateTime created;

	@Comment("생성자")
	@NotNull
	@Positive
	private Long creator;

	@PrePersist
	public void prePersist() {
		if (created == null) {
			created = ZonedDateTime.now();
		}
	}
}
