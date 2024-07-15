package com.kep.core.model.dto.issue;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "이슈 지원 정보")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueSupportDto {

	@Positive
	@Schema(description = "이슈 지원 아이디")
	private Long id;

	/**
	 * 상담 직원전환/검토 요청 구분
	 */
	@Schema(description = "상담 직원전환/검토 요청 구분")
	private IssueSupportType type;

	/**
	 * 상담 직원전환/검토 요청 상태
	 */
	@NotNull
	@Schema(description = "상담 직원전환/검토 요청 상태")
	private IssueSupportStatus status;

	/**
	 * 상담 검토/직원전환 요청 내용
	 */
	@Schema(description = "상담 검토/직원전환 요청 내용")
	private String question;

	@Schema(description = "질의 수정일시")
	private ZonedDateTime questionModified;

	/**
	 * 매니저 답변 내용
	 */
	@Schema(description = "매니저 답변 내용")
	private String answer;

	@Schema(description = "답변 수정일시")
	private ZonedDateTime answerModified;

	/**
	 * 상담직원전환 구분 값
	 */
	@Schema(description = "상담직원전환 구분 값")
	private IssueSupportChangeType changeType;

	/**
	 * 시스템전환시 branchId 저장
	 */
	@Schema(description = "시스템전환시 branchId 저장")
	private Long branchId;

	/**
	 * 시스템전환 - 소분류 카테고리 ID
	 */
	@Schema(description = "시스템전환 - 소분류 카테고리 ID")
	private Long categoryId;

	/**
	 * 상담직원선택 - 상담원ID
	 */
	@Schema(description = "상담직원선택 - 상담원ID")
	private Long selectMemberId;

	/**
	 * 이슈 상태
	 */
	@Schema(description = "이슈 상태")
	private IssueStatus issueStatus;

	/**
	 * 상담 가능 상태
	 */
	@Schema(description = "상담 가능 상태")
	private Boolean assignable;
}
