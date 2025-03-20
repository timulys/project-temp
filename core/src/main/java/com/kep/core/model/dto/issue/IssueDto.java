package com.kep.core.model.dto.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.channel.ChannelDto;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.customer.GuestDto;
import com.kep.core.model.dto.customer.IdentifyType;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.subject.IssueCategoryDto;
import io.swagger.v3.oas.annotations.media.Schema;
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

	@Schema(description = "이슈 아이디(PK)")
	@Positive
	private Long id;

	@Schema(description = "이슈 타입 ( chat , call , info )")
	private IssueType type;

	private Boolean relayed;

	private Boolean called;

	private Boolean videoCalled;

	@Schema(description = "이슈 상태 ( open : 상담 요청 , assign : 배정 완료 , close : 상담 종료 , ask : 고객 질의 , reply : 상담원 답변 , urgent : 지연 )")
	private IssueStatus status;

	private IssueCloseType closeType;

	@Schema(description = "브랜치 아이디")
	private Long branchId;

	@Schema(description = "채널 정보")
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

	@Schema(description = "미답변 메세지 카운트")
	@PositiveOrZero
	private Long askCount;

	@Schema(description = "생성 일자")
	private ZonedDateTime created;

	@Schema(description = "수정 일자")
	private ZonedDateTime modified;

	@Schema(description = "종료 일자")
	private ZonedDateTime closed;

	private Map<IssueSupportType, IssueSupportStatus> support;

	@Schema(description = "마지막 지원 요청")
	private IssueSupportDto lastIssueSupport;

	@Schema(description = "상담직원 이력")
	@JsonIncludeProperties({"id","username","nickname"})
	private List<MemberDto> supportMembers;

	@Schema(description = "상담 직원 전환/검토 카운트")
	private Long issueSupportCount;
}
