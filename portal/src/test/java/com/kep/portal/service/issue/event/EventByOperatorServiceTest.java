package com.kep.portal.service.issue.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.issue.*;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.dto.IssuePayloadTestFactory;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.channel.ChannelRepository;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.repository.issue.IssueLogRepository;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.client.PlatformClient;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.TypePredicates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.time.ZonedDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@Transactional
@Slf4j
class EventByOperatorServiceTest {
	@Resource
	private EventByOperatorService eventByOperatorService;
	@Resource
	private IssueLogRepository issueLogRepository;
	@Resource
	private BranchRepository branchRepository;
	@Resource
	private MemberRepository memberRepository;
	@Resource
	private IssueRepository issueRepository;
	@Resource
	private GuestRepository guestRepository;
	@Resource
	private ChannelRepository channelRepository;
	@Resource
	private ObjectMapper objectMapper;
	@MockBean
	private PlatformClient platformClient;
	@MockBean
	private SimpMessagingTemplate simpMessagingTemplate;
	@MockBean
	private EventBySystemService eventBySystemService;
	@MockBean
	private SecurityUtils securityUtils;

	private EasyRandom easyRandom;
	private final PlatformType platformType = PlatformType.kakao_counsel_talk;
	private final String serviceKey = "serviceKey";
	private final String userKey = "userKey";
	private Branch branch;
	private Channel channel;
	private Guest guest;
	private Member member;

	@BeforeEach
	void beforeEach() throws Exception {

		EasyRandomParameters parameters = new EasyRandomParameters();
		parameters.stringLengthRange(1, 30);
		parameters.collectionSizeRange(2, 5);
		parameters.excludeType(TypePredicates.ofType(Object.class));
		easyRandom = new EasyRandom(parameters);

		branch = branchRepository.save(Branch.builder()
				.name("TEST BRANCH")
				.assign(WorkType.Cases.branch)
				.enabled(true)
				.headQuarters(false)
				.offDutyHours(true)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.offDutyHours(true)
				.build());
		channel = channelRepository.save(Channel.builder()
				.name("TEST CHANNEL")
				.platform(platformType)
				.serviceId(serviceKey)
				.serviceKey(serviceKey)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		guest = guestRepository.save(Guest.builder()
				.channelId(channel.getId())
				.userKey(userKey)
				.created(ZonedDateTime.now())
				.build());

		member = memberRepository.save(Member.builder()
				.username("tester")
				.nickname("TEST MEMBER")
				.password("{noop}tester")
				.branchId(1L)
				.enabled(true)
				.status(WorkType.OfficeHoursStatusType.on)
				.creator(1L)
				.created(ZonedDateTime.now())
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());

		given(securityUtils.getMemberId())
				.willReturn(member.getId());
		given(platformClient.message(any(IssueDto.class), any(IssueLogDto.class)))
				.willReturn(true);
		given(platformClient.platformAnswer(any(IssueDto.class), any(IssuePayload.PlatformAnswer.class)))
				.willReturn(true);
		given(platformClient.close(any(IssueDto.class)))
				.willReturn(true);
//		doNothing().when(eventBySystemService.sendWelcome(any(Issue.class)));
		doNothing().when(simpMessagingTemplate).convertAndSend(any(String.class), any(IssueDto.class));
	}

	@Test
	@DisplayName("상담원 메세지 발송시, open 상태")
	void givenOpenStatus_whenOperatorMessageEvent() throws Exception {

		Issue issue = issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.open)
				.branchId(branch.getId())
				.channel(channel)
				.guest(guest)
				.firstAsked(ZonedDateTime.now())
				.statusModified(ZonedDateTime.now())
				.build());

		// ////////////////////////////////////////////////////////////////////
		// 상담원 메세지 발송전 상태
		// 직렬화 결과
		String previousIssueJson = objectMapper.writeValueAsString(issue);
		// 전체 메세지 카운트
		long previousTotalCount = issueLogRepository.count();
		// 미답변 메세지 카운트
		long previousAskCount = issue.getAskCount();
		// 미답변 시간
		ZonedDateTime firstAsked = issue.getFirstAsked();
		// 이슈 생성 시간
		ZonedDateTime created = issue.getCreated();
		// 이슈 변경 시간
		ZonedDateTime modified = issue.getModified();
		// 이슈 상태 변경 시간
		ZonedDateTime statusModified = issue.getStatusModified();

		// ////////////////////////////////////////////////////////////////////
		// 상담원 메세지 발송시, IllegalStateException 발생
		assertThrows(IllegalArgumentException.class, () -> {
			eventByOperatorService.message(issue.getId(), IssuePayloadTestFactory.getPayload());
		});

		// 직렬화 결과가 같아야 함
		assertEquals(previousIssueJson, objectMapper.writeValueAsString(issue));
		// 잔체 메세지 카운트 유지
		assertEquals(previousTotalCount, issueLogRepository.count());
		// 이슈 상태 유지
		assertEquals(IssueStatus.open, issue.getStatus());
		// 미답변 메세지 카운트 유지
		assertEquals(previousAskCount, issue.getAskCount());
		// 미답변 시간 유지
		assertEquals(firstAsked, issue.getFirstAsked());
		// 이슈 생성 시간 유지
		assertEquals(created, issue.getCreated());
		// 이슈 변경 시간 유지
		assertEquals(modified, issue.getModified());
		// 이슈 상태 변경 시간 유지
		assertEquals(statusModified, issue.getStatusModified());
	}

	@Test
	@DisplayName("상담원 메세지 발송시, close 상태")
	void givenCloseStatus_whenOperatorMessageEvent() throws Exception {

		Issue issue = issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.close)
				.branchId(branch.getId())
				.channel(channel)
				.guest(guest)
				.firstAsked(ZonedDateTime.now())
				.statusModified(ZonedDateTime.now())
				.build());

		// ////////////////////////////////////////////////////////////////////
		// 상담원 메세지 발송전 상태
		// 직렬화 결과
		String previousIssueJson = objectMapper.writeValueAsString(issue);
		// 전체 메세지 카운트
		long previousTotalCount = issueLogRepository.count();
		// 미답변 메세지 카운트
		long previousAskCount = issue.getAskCount();
		// 미답변 시간
		ZonedDateTime firstAsked = issue.getFirstAsked();
		// 이슈 생성 시간
		ZonedDateTime created = issue.getCreated();
		// 이슈 변경 시간
		ZonedDateTime modified = issue.getModified();
		// 이슈 상태 변경 시간
		ZonedDateTime statusModified = issue.getStatusModified();

		// ////////////////////////////////////////////////////////////////////
		// 상담원 메세지 발송시, IllegalStateException 발생
		assertThrows(IllegalArgumentException.class, () -> {
			eventByOperatorService.message(issue.getId(), IssuePayloadTestFactory.getPayload());
		});

		// 직렬화 결과가 같아야 함
		assertEquals(previousIssueJson, objectMapper.writeValueAsString(issue));
		// 전체 메세지 카운트 유지
		assertEquals(previousTotalCount, issueLogRepository.count());
		// 이슈 상태 유지
		assertEquals(IssueStatus.close, issue.getStatus());
		// 미답변 메세지 카운트 유지
		assertEquals(previousAskCount, issue.getAskCount());
		// 미답변 시간 유지
		assertEquals(firstAsked, issue.getFirstAsked());
		// 이슈 생성 시간 유지
		assertEquals(created, issue.getCreated());
		// 이슈 변경 시간 유지
		assertEquals(modified, issue.getModified());
		// 이슈 상태 변경 시간 유지
		assertEquals(statusModified, issue.getStatusModified());
	}

	@Test
	@DisplayName("상담원 메세지 발송시, assign 상태")
	void givenAssignStatus_whenOperatorMessageEvent() throws Exception {

		Issue issue = issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.assign)
				.branchId(branch.getId())
				.channel(channel)
				.guest(guest)
				.member(member)
				.firstAsked(ZonedDateTime.now())
				.statusModified(ZonedDateTime.now())
				.build());

		// ////////////////////////////////////////////////////////////////////
		// 상담원 메세지 발송전 상태
		// 전체 메세지 카운트
		long previousTotalCount = issueLogRepository.count();
		// 이슈 상태 변경 시간
		ZonedDateTime statusModified = issue.getStatusModified();
		// 미답변 시간
		ZonedDateTime firstAsked = issue.getFirstAsked();
		// 이슈 생성 시간
		ZonedDateTime created = issue.getCreated();
		// 이슈 변경 시간
		ZonedDateTime modified = issue.getModified();

		// ////////////////////////////////////////////////////////////////////
		// 상담원 메세지 발송시
		IssueDto issueDto = eventByOperatorService.message(issue.getId(), IssuePayloadTestFactory.getPayload());

		// 전체 메세지 카운트 1 증가
		assertEquals(previousTotalCount + 1, issueLogRepository.count());
		// 이슈 상태 변경 (reply)
		assertEquals(IssueStatus.reply, issueDto.getStatus());
		// 이슈 상태 변경 시간 변경
		assertTrue(statusModified.isBefore(issue.getStatusModified()));
		// 미답변 메세지 카운트 리셋
		assertEquals(0, issueDto.getAskCount());
		// 미답변 시간 NULL
		assertNull(issue.getFirstAsked());
		// 이슈 생성 시간 유지
		assertEquals(created, issueDto.getCreated());
//		// 이슈 변경 시간 유지
//		assertEquals(modified, issueDto.getModified());
		// 이슈 변경 시간 변경
		assertTrue(modified.isBefore(issueDto.getModified()));
	}

	@Test
	@DisplayName("상담원 메세지 발송시, ask 상태")
	void givenAskStatus_whenOperatorMessageEvent() throws Exception {

		Issue issue = issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.ask)
				.branchId(branch.getId())
				.channel(channel)
				.guest(guest)
				.member(member)
				.firstAsked(ZonedDateTime.now())
				.statusModified(ZonedDateTime.now())
				.build());

		// ////////////////////////////////////////////////////////////////////
		// 상담원 메세지 발송전 상태
		// 전체 메세지 카운트
		long previousTotalCount = issueLogRepository.count();
		// 이슈 상태 변경 시간
		ZonedDateTime statusModified = issue.getStatusModified();
		// 미답변 시간
		ZonedDateTime firstAsked = issue.getFirstAsked();
		// 이슈 생성 시간
		ZonedDateTime created = issue.getCreated();
		// 이슈 변경 시간
		ZonedDateTime modified = issue.getModified();

		// ////////////////////////////////////////////////////////////////////
		// 상담원 메세지 발송시
		IssueDto issueDto = eventByOperatorService.message(issue.getId(), IssuePayloadTestFactory.getPayload());

		// 전체 메세지 카운트 1 증가
		assertEquals(previousTotalCount + 1, issueLogRepository.count());
		// 이슈 상태 변경 (reply)
		assertEquals(IssueStatus.reply, issueDto.getStatus());
		// 이슈 상태 변경 시간 변경
		assertTrue(statusModified.isBefore(issue.getStatusModified()));
		// 미답변 메세지 카운트 리셋
		assertEquals(0, issueDto.getAskCount());
		// 미답변 시간 NULL
		assertNull(issue.getFirstAsked());
		// 이슈 생성 시간 유지
		assertEquals(created, issueDto.getCreated());
//		// 이슈 변경 시간 유지
//		assertEquals(modified, issueDto.getModified());
		// 이슈 변경 시간 변경
		assertTrue(modified.isBefore(issueDto.getModified()));
	}

	@Test
	@DisplayName("상담원 메세지 발송시, reply 상태")
	void givenReplyStatus_whenOperatorMessageEvent() throws Exception {

		Issue issue = issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.reply)
				.branchId(branch.getId())
				.channel(channel)
				.guest(guest)
				.member(member)
				.firstAsked(ZonedDateTime.now())
				.statusModified(ZonedDateTime.now())
				.build());

		// ////////////////////////////////////////////////////////////////////
		// 상담원 메세지 발송전 상태
		// 전체 메세지 카운트
		long previousTotalCount = issueLogRepository.count();
		// 이슈 상태 변경 시간
		ZonedDateTime statusModified = issue.getStatusModified();
		// 미답변 시간
		ZonedDateTime firstAsked = issue.getFirstAsked();
		// 이슈 생성 시간
		ZonedDateTime created = issue.getCreated();
		// 이슈 변경 시간
		ZonedDateTime modified = issue.getModified();

		// ////////////////////////////////////////////////////////////////////
		// 상담원 메세지 발송시
		IssueDto issueDto = eventByOperatorService.message(issue.getId(), IssuePayloadTestFactory.getPayload());

		// 전체 메세지 카운트 1 증가
		assertEquals(previousTotalCount + 1, issueLogRepository.count());
		// 이슈 상태 유지
		assertEquals(IssueStatus.reply, issueDto.getStatus());
		// 이슈 상태 변경 시간 변경
		assertEquals(statusModified, issue.getStatusModified());
		// 미답변 메세지 카운트 리셋
		assertEquals(0, issueDto.getAskCount());
		// 미답변 시간 NULL
		assertNull(issue.getFirstAsked());
		// 이슈 생성 시간 유지
		assertEquals(created, issueDto.getCreated());
//		// 이슈 변경 시간 유지
//		assertEquals(modified, issueDto.getModified());
		// 이슈 변경 시간 변경
		assertTrue(modified.isBefore(issueDto.getModified()));
	}

	@Test
	void testClose() throws Exception {

		// given
		Issue issue = issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.reply)
				.branchId(branch.getId())
				.channel(channel)
				.guest(guest)
				.member(member)
				.firstAsked(ZonedDateTime.now())
				.statusModified(ZonedDateTime.now())
				.build());

		// when
		IssueDto issueDto = eventByOperatorService.close(issue.getId(), Collections.emptyMap());

		// then
		assertEquals(issue.getId(), issueDto.getId());
		assertEquals(IssueStatus.close, issueDto.getStatus());
		assertNotNull(issueDto.getClosed());
		assertTrue(issueDto.getClosed().isAfter(issueDto.getCreated()));
		assertEquals(0, issueDto.getAskCount());
		assertNull(issue.getFirstAsked());
	}
}
