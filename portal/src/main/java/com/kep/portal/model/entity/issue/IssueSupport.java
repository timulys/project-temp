package com.kep.portal.model.entity.issue;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.issue.IssueSupportChangeType;
import com.kep.core.model.dto.issue.IssueSupportStatus;
import com.kep.core.model.dto.issue.IssueSupportType;
import com.kep.portal.model.entity.member.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.Table;
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
@Table(
		indexes = {
				@Index(name = "IDX_ISSUE_SUPPORT__SEARCH_MEMBER", columnList = "questioner, questionModified, created desc")
				, @Index(name = "IDX_ISSUE_SUPPORT__SEARCH_DATE", columnList = "questionModified, questioner, created desc")
				, @Index(name = "IDX_ISSUE_SUPPORT__ISSUE_TYPE", columnList = "issueId , type")
				, @Index(name = "IDX_ISSUE_SUPPORT__MEMBER_ID", columnList = "memberId")
		}
)
public class  IssueSupport {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("상담 지원 요청 PK")
	@Positive
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issueId", foreignKey = @ForeignKey(name="FK_ISSUE_SUPPORT__ISSUE"), nullable = false)
	@Comment("이슈 PK")
	private Issue issue;

	@Comment("상담 요청 구분 - 상담검토요청, 상담직원전환요청")
	@NotNull
	@Enumerated(EnumType.STRING)
	private IssueSupportType type;

	@Comment("상담 검토/직원전환 요청 상태")
	@NotNull
	@Enumerated(EnumType.STRING)
	private IssueSupportStatus status;

	@Comment("상담 검토/직원전환 요청 내용")
	@Size(max = 500)
	private String question;

	@Comment("상담 검토/직원전환 요청 유저 PK")
	@Positive
	private Long questioner;

	@Comment("상담 검토/직원전환 요청 시간")
	private ZonedDateTime questionModified;

	@Comment("그룹장 답변 내용")
	@Size(max = 500)
	private String answer;

	@Comment("그룹장 유저 PK")
	@Positive
	private Long answerer;

	@Comment("그룹장 답변 시간")
	private ZonedDateTime answerModified;

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
	@JoinColumn(name = "memberId", foreignKey = @ForeignKey(name="FK_ISSUE_SUPPORT__MEMBER"))
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
