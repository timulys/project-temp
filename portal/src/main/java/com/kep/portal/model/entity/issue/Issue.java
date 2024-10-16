package com.kep.portal.model.entity.issue;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.portal.event.IssueStatisticsEventListener;
import com.kep.portal.event.MemberEventListener;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.issue.IssueCloseType;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueSupportStatus;
import com.kep.core.model.dto.issue.IssueSupportType;
import com.kep.core.model.dto.issue.IssueType;
import com.kep.portal.model.converter.BooleanConverter;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.subject.IssueCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 채팅방, 알림톡, 친구톡, 챗봇, 음성봇 등에 대응
 */
@Entity
@Table(indexes = { @Index(name = "IDX_ISSUE__SEARCH", columnList = "memberId, status, modified")
		, @Index(name = "IDX_ISSUE__SEARCH_DATE", columnList = "modified, memberId, status")
		, @Index(name = "IDX_ISSUE__PLATFORM", columnList = "guestId, status, channelId")
		, @Index(name = "IDX_ISSUE__MANAGE_MEMBER", columnList = "branchId, teamId, memberId, status")
		, @Index(name = "IDX_ISSUE__MANAGE_CATEGORY", columnList = "branchId, teamId, issueCategoryId, status")
		, @Index(name = "IDX_ISSUE__MANAGE_CREATED", columnList = "branchId, created, status")
		, @Index(name = "IDX_ISSUE__MANAGE_ENDED", columnList = "branchId, closed, status")
		, @Index(name = "IDX_ISSUE__MEMBER_FIRST_ASKED", columnList = "memberFirstAsked")
		, @Index(name = "IDX_ISSUE__TYPE", columnList = "type")
})

@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(IssueStatisticsEventListener.class)
public class Issue {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Enumerated(EnumType.STRING)
	@Comment("타입 (chat: 채팅, call: 콜)")
	@NotNull
	private IssueType type;

	@Comment("상태 (relay: 외부연동, open: 상담요청, assign: 배정완료, close: 종료, ask: 고객질의, reply: 상담원응대, urgent: 고객 질의 중 미답변 시간 초과)")
	@Enumerated(EnumType.STRING)
	@NotNull
	private IssueStatus status;

	@Enumerated(EnumType.STRING)
	@Comment("타입 (guest: 고객 종료, operator: 상담원 종료, manager: 매니저 죵료, system: 시스템 종료)")
	private IssueCloseType closeType;

	/**
	 * 브랜치
	 */
	@Comment("브랜치 PK")
	@NotNull
	@Positive
	private Long branchId;

	/**
	 * 채널
	 */
	@ManyToOne
	@JoinColumn(name = "channelId", foreignKey = @ForeignKey(name = "FK_ISSUE__CHANNEL"), nullable = false, updatable = false)
	@Comment("채널 PK")
	@NotNull
	private Channel channel;

	/**
	 * 분류
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "issueCategoryId", foreignKey = @ForeignKey(name = "FK_ISSUE__ISSUE_CATEGORY"))
	@Comment("뷴류 PK")
	@JsonIgnoreProperties({ "parent", "path" })
	private IssueCategory issueCategory;

	/**
	 * 상담그룹
	 */
	@Comment("상담그룹 PK")
	private Long teamId;

	/**
	 * 유저
	 */
	@ManyToOne
	@JoinColumn(name = "memberId", foreignKey = @ForeignKey(name = "FK_ISSUE__MEMBER"))
	@Comment("유저 PK")
	@JsonIgnoreProperties({ "memberOfficeHours" })
	private Member member;

	/**
	 * 고객
	 */
	@ManyToOne
	@JoinColumn(name = "guestId", foreignKey = @ForeignKey(name = "FK_ISSUE__GUEST"), nullable = false) // , updatable = false)
	@Comment("비식별 고객 PK")
	@NotNull
	private Guest guest;

	/**
	 * 식별 고객, 검색이나 특별한 요건을 처리하기 위한 비정규 컬럼이므로 사용 자제 ({@link Guest#customer}) 사용
	 */
	@Comment("식별 고객 PK")
	private Long customerId;

	/**
	 * 오픈빌더 등 외부 연동 여부
	 */
	@Column(length = 1)
	@Comment("외부 연동 여부")
	@Convert(converter = BooleanConverter.class)
	private Boolean relayed;

	/**
	 * 전화 상담 여부
	 */
	@Column(length = 1)
	@Comment("전화 상담 여부")
	@Convert(converter = BooleanConverter.class)
	private Boolean called;

	/**
	 * 화상 상담 여부
	 */
	@Column(length = 1)
	@Comment("화상 상담 여부")
	@Convert(converter = BooleanConverter.class)
	private Boolean videoCalled;

//	/**
//	 * 마지막 인증 방식
//	 */
//	@Enumerated(EnumType.STRING)
//	@Comment("고객 인증 방식")
//	private IdentifyType identifyType;

	@OneToOne
	@JoinColumn(name = "issueExtraId", foreignKey = @ForeignKey(name = "FK_ISSUE__ISSUE_EXTRA"))
	@Comment("상세 정보 (부가 정보)")
	private IssueExtra issueExtra;

//	/**
//	 * 유입 경로 | 진입 파라미터
//	 */
//	@Size(max = 500)
//	private String referer;

	/**
	 * 마지막 로그
	 */
	@ManyToOne
	@JoinColumn(name = "lastIssueLogId", foreignKey = @ForeignKey(name = "FK_ISSUE__ISSUE_LOG"))
	@Comment("마지막 대화 PK")
	private IssueLog lastIssueLog;

	/**
	 * 미답변 고객 메세지 카운트
	 */
	@ColumnDefault(value = "0")
	@Comment("미답변 고객 메세지 카운트")
	@NotNull
	private Long askCount; // TODO: Integer

	/**
	 * 배정 시도 카운트
	 */
	@ColumnDefault(value = "0")
	@Comment("배정 시도 카운트")
	@NotNull
	private Integer assignCount;

	// TODO: 과금 여부
//	@Column(length = 1)
//	@Comment("과금 여부")
//	@Convert(converter = BooleanConverter.class)
//	private Boolean charged;

	/**
	 * 생성 일시
	 */
	@Column(updatable = false)
	@Comment("생성 일시")
	@NotNull
	private ZonedDateTime created;

	/**
	 * TODO: 변경해야하는 이벤트 일괄 적용 변경 일시, 목록 정렬값 (ex, 생성, 배정, 고객 메세지 등)
	 */
	@Comment("수정 일시, 목록 기본 정렬값")
	@NotNull
	private ZonedDateTime modified;

	/**
	 * 채팅 조회 시 2023.04.10 / philip.lee7 / 채팅 마지막 메모 PK COLUMN 추가
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "last_issue_memo")
	@Comment("채팅 마지막 메모")
	private IssueMemo lastIssueMemo;

	/**
	 * {@link Issue#status}에 영향을 주는 이벤트시
	 */
	@Comment("상태 변경 일시")
	private ZonedDateTime statusModified;

	/**
	 * 미답변 고객 메세지 중, 최초 메세지 생성 일시
	 */
	@Comment("미답변 고객 메세지 중, 최초 메세지 생성 일시")
	private ZonedDateTime firstAsked;

	/**
	 * 종료 일시
	 */
	@Comment("종료 일시")
	private ZonedDateTime closed;


	@Comment("회원 최초 메세지 생성일")
	private ZonedDateTime memberFirstAsked;

	@Column(length =1, name="send_flag")
	@Comment("고객정보 요청 flag(Y:알림 발송,N:알림 X")

	private String sendFlag;

	/**
	 * 이슈 지원 목록
	 */
	@Transient
	private Map<IssueSupportType, IssueSupportStatus> support;

	@Transient
	private List<Member> supportMembers;

	@Transient
	private Long issueSessionDay;

	@Transient
	private Long chatCount;

	// todo 다 팩토링 할 부분 ( 급하여서 @Transient 추가... dto로 변경 후 제거 예정....)
	@Transient
	private Long issueSupportCount;


	@PrePersist
	@PreUpdate
	public void prePersist() {
		if (created == null) {
			created = ZonedDateTime.now();
		}
		if (modified == null) {
			modified = ZonedDateTime.now();
		}
		if (askCount == null) {
			askCount = 0L;
		}
		if (assignCount == null) {
			assignCount = 0;
		}
		//2023/12/22 bnk 추가
		if (sendFlag == null) {
			sendFlag = "Y"; // 이슈가 생성될 때 기본값
		}
	}
	// 멤버 ID 리스트를 저장할 새 필드
	@Transient  // 데이터베이스에 저장하지 않음.
	private List<Long> memberIds;
}
