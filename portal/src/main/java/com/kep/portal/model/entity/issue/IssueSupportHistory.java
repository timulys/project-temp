package com.kep.portal.model.entity.issue;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.issue.IssueSupportChangeType;
import com.kep.core.model.dto.issue.IssueSupportStatus;
import com.kep.core.model.dto.issue.IssueSupportType;
import com.kep.portal.model.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
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
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
		indexes = {
				@Index(name = "IDX_ISSUE_SUPPORT_HISTORY__SEARCH_DETAIL", columnList = "created, issueId")
		}
)
public class IssueSupportHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("상담 이력 PK")
	@Positive
	private Long id;

	@Comment("상담 지원 요청 PK")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issueSupportId", foreignKey = @ForeignKey(name="FK_ISSUE_SUPPORT_HISTORY__ISSUE_SUPPORT"), nullable = false)
	private IssueSupport issueSupport;

	@Comment("이슈 PK")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issueId", foreignKey = @ForeignKey(name="FK_ISSUE_SUPPORT_HISTORY__ISSUE"), nullable = false)
	private Issue issue;

	@Comment("상담 요청 구분 - 상담검토요청, 상담직원전환요청")
	@NotNull
	@Enumerated(EnumType.STRING)
	private IssueSupportType type;

	@Comment("상담 검토/직원전환 요청 상태")
	@NotNull
	@Enumerated(EnumType.STRING)
	private IssueSupportStatus status;

	@Comment("상담 검토/직원전환 요청/답변 내용")
	@Size(max = 500)
	private String content;

	@Comment("상담직원전환 구분 값")
	@Enumerated(EnumType.STRING)
	private IssueSupportChangeType changeType;

	@Comment("시스템전환시 브랜치 PK")
	private Long branchId;

	@Comment("시스템전환 소분류 카테고리 PK")
	@Positive
	private Long categoryId;

	@Comment("상담직원선택 유저 PK")
	@Positive
	private Long selectMemberId;

	@Comment("배정된 유저 PK")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId", foreignKey = @ForeignKey(name="FK_ISSUE_SUPPORT_HISTORY__MEMBER"))
	@JsonIncludeProperties({"id", "username","nickname"})
	private Member member;

	@Comment("등록 유저 PK")
	@NotNull
	@Positive
	private Long creator;

	@Comment("등록 시간")
	@NotNull
	private ZonedDateTime created;
}
