package com.kep.portal.model.entity.issue;

import com.kep.portal.model.entity.subject.IssueCategory;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * 이슈, 상세 정보
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(indexes = {
		@Index(name = "IDX_ISSUE_EXTRA__SEARCH", columnList = "guestId, inflow")
})
public class IssueExtra {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	/**
	 * 상담 보조 도구에서 검색 필요
	 */
	@Comment("비식별 고객 PK")
	@Positive
//	@Deprecated
	// TODO: 메모 목록은 고객사 정보 활용, 이외 사용하는 곳 없으면 삭제
	private Long guestId;

	/**
	 * 후처리 요약
	 */
	@Comment("후처리 요약")
	@Size(max = 1000)
	private String summary;

	@OneToOne(fetch = FetchType.LAZY)
	private IssueSummaryCategory issueSummaryCategory;

	/**
	 * 후처리 분류
	 */
	@Comment("후처리 분류")
	@Positive
	private Long issueCategoryId;
	@Transient
	private IssueCategory issueCategory;
	/**
	 * 후처리 수정 일시
	 */
	@Comment("후처리 수정 일시")
	private ZonedDateTime summaryModified;

	/**
	 * [2023.05.09/asher.shin/요약 완료 여부]
	 */
	private boolean summaryCompleted;
	/**
	 * 메모
	 */
	@Comment("메모")
	@Size(max = 1000)
	private String memo;
	/**
	 * 메모 수정 일시
	 */
	@Comment("메모 수정 일시")
	private ZonedDateTime memoModified;

	/**
	 * 이슈 생성시, 파라미터 (Stringify JSON)
	 */
	@Comment("이슈 생성시, 파라미터 (Stringify JSON)")
	@Size(max = 1000)
	private String parameter;

	/**
	 * 이슈 생성시, 파라미터 중 유입경로 (Stringify JSON)
	 */
	@Comment("이슈 생성시, 파라미터 중 유입경로 (Stringify JSON)")
	private String inflow;
	/**
	 * 이슈 생성시, 파라미터 중 유입경로 생성 일시
	 */
	@Comment("이슈 생성시, 파라미터 중 유입경로 생성 일시")
	private ZonedDateTime inflowModified;

	@Comment("고객 상담 평가, 점수")
	private Integer evaluationScore;
	@Comment("고객 상담 평가 생성 일시")
	private ZonedDateTime evaluationModified;
}
