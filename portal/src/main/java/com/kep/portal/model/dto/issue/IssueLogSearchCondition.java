package com.kep.portal.model.dto.issue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 이슈 로그 검색 조건
 */
@Schema(description = "이슈 로그 검색 조건")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueLogSearchCondition {

	@Schema(description = "이슈 아이디", required = true)
	private Long issueId;

	@Schema(description = "이슈 로그 아이디")
	private Long issueLogId;

	@Schema(description = "")
	private String direction;

	// ////////////////////////////////////////////////////////////////////////
	// issueId -> guestId -> issueIds
	@Schema(description = "이슈 아이디 목록")
	private List<Long> issueIds;

	// ////////////////////////////////////////////////////////////////////////
	// 담당 유저
	@Schema(description = "담당자 아이디 목록")
	private List<Long> assigners;
}
