package com.kep.core.model.dto.issue;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueLogDto {

	@Positive
	private Long id;

	@NotNull
	@Positive
	private Long issueId;

	@NotNull
	private IssueLogStatus status;

	/**
	 * 연관 issueLogId
	 * 챗봇 응답, 설문조사 등
	 */
	@Positive
	@Positive
	private Long relativeId;

	@NotNull
	private String payload;

	@NotNull
	@Positive
	private Long creator;

	@NotNull
	private ZonedDateTime created;
}
