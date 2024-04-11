package com.kep.core.model.dto.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * 채팅방, 알림톡, 친구톡, 챗봇, 음성봇 등에 대응
 * @수정일자	  / 수정자			 / 수정내용
 * 2023.05.31 / asher.shin	 / 고객정보 추가
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueDto {

	@Positive
	private Long id;

	private IssueType type;

	private Boolean relayed;

	private Boolean called;

	private Boolean videoCalled;

	private IssueStatus status;

	private IssueCloseType closeType;

	private Long branchId;

	private ChannelDto channel;

//	@Positive
//	private Long issueCategoryId;

	private IssueCategoryDto issueCategory;

//	@NotNull
//	private Long teamId;

	@JsonIgnoreProperties({"roles", "memberRoles"})
	private MemberDto member;

	private GuestDto guest;

	private Long customerId;

	private CustomerDto customer;

	private IdentifyType identifyType;

	@Size(max = 500)
	private String title;

	private IssueExtraDto issueExtra;



//	@Size(max = 500)
//	private String referer;

	private IssueLogDto lastIssueLog;
	//[2023.04.12] 마지막 채팅 메모 추가
	private IssueMemoDto lastIssueMemo;

	@PositiveOrZero
	private Long askCount;

	private ZonedDateTime created;

	private ZonedDateTime modified;

	private ZonedDateTime closed;

	private Map<IssueSupportType, IssueSupportStatus> support;

	@JsonIncludeProperties({"id","username","nickname"})
	private List<MemberDto> supportMembers;
}
