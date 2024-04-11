package com.kep.portal.model.dto.issue;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueType;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 이슈 검색 조건
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueSearchCondition {

	private Long branchId;

	private Long teamId;

	private String dateSubject;

	private LocalDate startDate;

	private LocalDate endDate;

	private PlatformType platform;

	private Boolean relayed;

	private Boolean called;

	private Boolean videoCalled;

	private List<Long> memberId;

	private Long categoryId;

	private List<IssueStatus> status;

	private List<IssueStatus> notStatus;

	private String customerSubject;

	private String customerQuery;

	private Long guestId;

	// ////////////////////////////////////////////////////////////////////////
	// platform -> channels
	private List<Channel> channels;
	// customerSubject, customerQuery -> guests
	private List<Guest> guests;
	
	//BNK 싱크되지 않은 비식별 고객 목록
    private List<Guest> unSyncedGuests;
	
	private List<Long> guestIds;
	// customerSubject, customerQuery -> customers
	// 비식별고객도 이름으로 검색 추가
	@Deprecated
	private List<Long> customerIds;
	// memberId -> members
	private List<Member> members;

	private IssueType type;
}
