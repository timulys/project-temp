package com.kep.core.model.dto.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

/**
 * 이슈 지원 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueSupportDto {

	@Positive
	private Long id;

	/**
	 * 상담 직원전환/검토 요청 구분
	 */
	private IssueSupportType type;

	/**
	 * 상담 직원전환/검토 요청 상태
	 */
	@NotNull
	private IssueSupportStatus status;

	/**
	 * 상담 검토/직원전환 요청 내용
	 */
	private String question;

	private ZonedDateTime questionModified;

	/**
	 * 매니저 답변 내용
	 */
	private String answer;

	private ZonedDateTime answerModified;

	/**
	 * 상담직원전환 구분 값
	 */
	private IssueSupportChangeType changeType;

	/**
	 * 시스템전환시 branchId 저장
	 */
	private Long branchId;

	/**
	 * 시스템전환 - 소분류 카테고리 ID
	 */
	private Long categoryId;

	/**
	 * 상담직원선택 - 상담원ID
	 */
	private Long selectMemberId;

	/**
	 * 이슈 상태
	 */
	private IssueStatus issueStatus;

	/**
	 * 상담 가능 상태
	 */
	private Boolean assignable;
}
