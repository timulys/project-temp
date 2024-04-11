package com.kep.portal.model.entity.statistics;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author 상담 현황 통계
 *
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReplyStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Column(length = 12)
	@Comment("PK, 고객 인입 일시 그룹(10분 간격), 예시 : 2023년 3월 22일 9시 10분대 202303220910")
	private String timeGroup;

	@Comment("지점 Id")
	private Long branchId;

	@PositiveOrZero
	@Comment("해당 시간 대 인입건수")
	private Long entryCount;

	@PositiveOrZero
	@Comment("해당 시간 대 상담 첫 응답건수")
	private Long replyCount;

	@PositiveOrZero
	@Comment("해당 시간 대 인입한 고객과의 상담소요시간 평균(초)")
	private Long averageCounselTime;// 지속적으로 업데이트가 발생함

	@Comment("데이터 생성 시각")
	private ZonedDateTime created;

	@Comment("데이터 수정 시각")
	private ZonedDateTime modified;

	@Comment("상담 놓침 건수")
	private Long missingCount;

	@PrePersist
	public void prePersist() {
		if ( entryCount == null )
			entryCount = 0L;
		
		if ( replyCount == null )
			replyCount = 0L;		
		
		if (averageCounselTime == null)
			averageCounselTime = 0L;

		if (missingCount == null)
			missingCount = 0L;

		if (created == null)
			created = ZonedDateTime.now();
	}

	@PreUpdate
	public void PreUpdate() {
		modified = ZonedDateTime.now();
	}
}
