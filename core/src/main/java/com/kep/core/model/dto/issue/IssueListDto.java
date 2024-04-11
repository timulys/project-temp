package com.kep.core.model.dto.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.channel.ChannelDto;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.customer.GuestDto;
import com.kep.core.model.dto.customer.IdentifyType;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.subject.IssueCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * 채팅방, 알림톡, 친구톡, 챗봇, 음성봇 등에 대응
 * 상담 목록용
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueListDto {

	private Long id;

	private IssueType type;

	private Boolean relayed;

	private IssueStatus status;

	private BranchDto branch;

	private ChannelDto channel;

	private MemberDto member;

	private GuestDto guest;

	@JsonIgnoreProperties({"customerContacts"})
	private CustomerDto customer;

	private IssueExtraDto issueExtra;

	private IssueLogDto lastIssueLog;

	@PositiveOrZero
	private Long askCount;

	private ZonedDateTime created;

	private ZonedDateTime modified;

	private ZonedDateTime closed;
}
