package com.kep.portal.model.dto.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 이슈 로그 검색 조건
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueLogSearchCondition {

	private Long issueId;

	private Long issueLogId;

	private String direction;

	// ////////////////////////////////////////////////////////////////////////
	// issueId -> guestId -> issueIds
	private List<Long> issueIds;

	// ////////////////////////////////////////////////////////////////////////
	// 담당 유저
	private List<Long> assigners;
}
