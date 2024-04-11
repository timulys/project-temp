package com.kep.portal.model.entity.issue;

import com.kep.core.model.dto.issue.IssueLogStatus;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

/**
 * 이슈 로그
 */
@Entity
// TODO: id 로 정렬 가능, created 제외 가능
@Table(indexes = {
		@Index(name = "IDX_ISSUE_LOG__SEARCH", columnList = "issueId, id, created"),
		@Index(name = "IDX_ISSUE_LOG__ASSIGNER", columnList = "issueId, assigner, id, created"),
		@Index(name = "IDX_ISSUE_LOG__ISSUE", columnList = "issueId, created")
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IssueLog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("이슈 PK")
	@NotNull
	@Positive
	private Long issueId;

	@Comment("상태 (send: 발신, fail: 실패, receive: 수신, read: 읽음)")
	@Enumerated(EnumType.STRING)
	private IssueLogStatus status;

	@Comment("연관된 로그 PK")
	@Positive
	private Long relativeId; // issueLogId

	// TODO: 전체 JSON > IssuePayload.text(= 1000)
	// TODO: CLOB 고려, 외부 데이터를 쌓을 수도 있음
//	@Column(length = 4000)
//	@Comment("로그 페이로드")
//	private String payload;

	//	- 변경 / YO / 231113
	@Comment("로그 페이로드")
	@Lob
	@Basic
	@Nationalized
	@NotNull
	private String payload;

	@Comment("담당 유저, 유저 PK")
	@Positive
	private Long assigner;

	@Comment("생성자")
	@NotNull
	@Positive
	private Long creator;

	@Comment("생성 일시")
	@NotNull
	private ZonedDateTime created;
}