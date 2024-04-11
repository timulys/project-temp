package com.kep.portal.model.dto.platform;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 플랫폼 템플릿 목록 검색 조건
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformTemplateCondition {
	private Long branchId;

	private String name;

	private List<PlatformTemplateStatus> status;

	private PlatformType platform;
}
