package com.kep.portal.model.entity.statistics;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.Positive;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GuestWaitingTime {
	@Id
	@Positive
	@Comment("issue PK")
	private Long issueId;

	@Positive
	@Column(updatable = false)
	@Comment("브랜치 PK")
	private Long branchId;

	@Column(updatable = false)
	@Comment("고객 인입 일시")
	private ZonedDateTime entryTime;

	@Column(updatable = false)
	@Comment("대화 시작 일시")
	private ZonedDateTime firstReplyTime;

	@Column(updatable = false)
	@Comment("고객 인입 일시 그룹(10분 간격), 예시 : 2023년 3월 22일 9시 10분대 202303220910")
	private String entryTimeGroup;

	@Column(updatable = false)
	@Comment("고객 인입 일시~대화 시작 일시 초")
	private Long waitingTime;

	@PrePersist
	public void prePersist() {
		if (firstReplyTime == null)
			firstReplyTime = ZonedDateTime.now();

		if (entryTimeGroup == null)
			entryTimeGroup = entryTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")).substring(0, 11) + "0";

		if (waitingTime == null)
			waitingTime = Duration.between(entryTime, firstReplyTime).getSeconds();
	}
}
