package com.kep.core.model.dto.issue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueLogDto {

	@Schema(description = "이슈 로그 아이디 (PK)")
	@Positive
	private Long id;

	@Schema(description = "이슈 아이디")
	@NotNull
	@Positive
	private Long issueId;

	@Schema(description = "메세지 보낸 상태( send : 상담원 메세지 발송 , receive : 고객 메세지 발송 )")
	@NotNull
	private IssueLogStatus status;

	/**
	 * 연관 issueLogId
	 * 챗봇 응답, 설문조사 등
	 */
	@Positive
	@Positive
	private Long relativeId;

	@Schema(description = "전송 메세지 ( chapters > sections > data에 메세지 존재 ) ( json 형식 )")
	@NotNull
	private String payload;

	@Schema(description = "생성자 아이디")
	@NotNull
	@Positive
	private Long creator;

	@Schema(description = "생성일자")
	@NotNull
	private ZonedDateTime created;
}
