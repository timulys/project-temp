package com.kep.portal.model.dto.issue;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueType;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 이슈 검색 조건
 *
 * FIXME :: DTO에 Entity를 직접 받아 사용함. 수정 필요 20240712 volka
 */
@Schema(description = "이슈 검색 조건")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueSearchCondition {

	@Schema(description = "브랜치 아이디")
	private Long branchId;

	@Schema(description = "팀 아이디")
	private Long teamId;

	@Schema(description = "검색 기준 변수명 (created, modified)")
	private String dateSubject;

	@Schema(description = "시작일")
	private LocalDate startDate;

	@Schema(description = "종료일")
	private LocalDate endDate;

	@Schema(description = "플랫폼 타입 [solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template, legacy_web, legacy_app , kakao_counsel_center]")
	private PlatformType platform;

	@Schema(description = "릴레이 여부")
	private Boolean relayed;

	@Schema(description = "전화상담 여부")
	private Boolean called;

	@Schema(description = "영상통화 여부")
	private Boolean videoCalled;

	@Schema(description = "사용자 아이디")
	private List<Long> memberId;

	@Schema(description = "이슈 카테고리 아이디")
	private Long categoryId;

	@Schema(description = "이슈 상태 목록 [open : 상담 요청 , assign : 배정 완료, close : 상담 종료(완료),  ask : 고객 질의,  reply : 상담원 답변, urgent : 고객 질의 중 미답변 시간 초과]")
	private List<IssueStatus> status;

	@Schema(description = "이슈 상태 미해당 목록 [open : 상담 요청 , assign : 배정 완료, close : 상담 종료(완료),  ask : 고객 질의,  reply : 상담원 답변, urgent : 고객 질의 중 미답변 시간 초과]")
	private List<IssueStatus> notStatus;

	@Schema(description = "고정")
	private String customerSubject;

	@Schema(description = "고객이름")
	private String customerQuery;

	@Schema(description = "게스트 아이디")
	private Long guestId;

	// tim.c : 대화 내용 검색용
	@Schema(description = "대화 내용")
	private String payload;

	// ////////////////////////////////////////////////////////////////////////
	// platform -> channels
	@Schema(description = "채널 목록")
	private List<Channel> channels;
	// customerSubject, customerQuery -> guests
	@Schema(description = "게스트 목록")
	private List<Guest> guests;
	
	//BNK 싱크되지 않은 비식별 고객 목록
	//FIXME :: BNK 비즈니스. 걷어내야함, 사용되는 곳 없음. 20240712 volka
    @Schema(description = "BNK 싱크되지 않은 비식별 고객 목록")
	private List<Guest> unSyncedGuests;

	//FIXME :: 사용되는 곳 없음. 20240712 volka
	@Schema(description = "게스트 아이디 목록")
	private List<Long> guestIds;

	// customerSubject, customerQuery -> customers
	// 비식별고객도 이름으로 검색 추가
	//FIXME :: 사용되는 곳 없음. 20240712 volka
	@Deprecated
	@Schema(description = "고객 아이디 목록 [deprecated]")
	private List<Long> customerIds;
	// memberId -> members
	@Schema(description = "사용자 목록")
	private List<Member> members;

	@Schema(description = "")
	private IssueType type;


	@Schema(description = "채널 아이디")
	private Long channelId;
}
