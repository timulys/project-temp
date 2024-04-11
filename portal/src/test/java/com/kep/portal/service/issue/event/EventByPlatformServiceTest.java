package com.kep.portal.service.issue.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueType;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.dto.IssuePayloadTestFactory;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.channel.ChannelRepository;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.repository.issue.IssueLogRepository;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.service.issue.IssueService;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.TypePredicates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@Transactional
@Slf4j
class EventByPlatformServiceTest {
	@Resource
	private EventByPlatformService eventByPlatformService;
	@Resource
	private IssueLogRepository issueLogRepository;
	@Resource
	private BranchRepository branchRepository;
	@Resource
	private IssueRepository issueRepository;
	@Resource
	private GuestRepository guestRepository;
	@Resource
	private ChannelRepository channelRepository;
	@Resource
	private IssueService issueService;
	@Resource
	private ObjectMapper objectMapper;
	@MockBean
	private SimpMessagingTemplate simpMessagingTemplate;
	@MockBean
	private EventBySystemService eventBySystemService;

	private EasyRandom easyRandom;
	private final PlatformType platformType = PlatformType.kakao_counsel_talk;
	private final String serviceKey = "serviceKey";
	private final String userKey = "userKey";
	private Branch branch;
	private Channel channel;
	private Guest guest;

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

		doNothing().when(simpMessagingTemplate).convertAndSend(any(String.class), any(IssueDto.class));
	}

	@Test
	@DisplayName("상담 요청시, 진행중인 이슈가 있을 경우, 해당 이슈를 리턴해야한다")
	void givenNotClosedIssue_whenOpenEvent_thenReturnExistIssue() throws Exception {

		Issue issue = issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.assign)
				.branchId(branch.getId())
				.channel(channel)
				.guest(guest)
				.firstAsked(ZonedDateTime.now())
				.statusModified(ZonedDateTime.now())
				.build());

		// 진행중인 이슈가 있음
		Issue ongoingIssue = issueService.findOngoingByPlatform(channel.getId(), guest.getId());
		assertNotNull(ongoingIssue);
		assertNotEquals(IssueStatus.close, ongoingIssue.getStatus());

		// 상담 요청시, 기존에 진행중인 이슈를 리턴해야한다
		IssueDto issueDto = eventByPlatformService.open(platformType, serviceKey, userKey, Collections.emptyMap(), System.currentTimeMillis());
		assertNotNull(issueDto);
		assertEquals(issue.getId(), issueDto.getId());
		assertEquals(IssueStatus.assign, issueDto.getStatus());
	}

	@Test
	@DisplayName("상담 요청시, 진행중인 이슈가 없을 경우, 새 이슈를 리턴해야한다")
	@Disabled("Platform 서비스 실행 필요")
	void whenOpenEvent_thenReturnNewIssue() throws Exception {

		// 진행중인 이슈가 없음
		Issue ongoingIssue = issueService.findOngoingByPlatform(channel.getId(), guest.getId());
		assertNull(ongoingIssue);

		// 전체 이슈 카운트
		long previousTotalCount = issueRepository.count();

		// 상담 요청시, 새 이슈를 리턴해야한다
		IssueDto issueDto = eventByPlatformService.open(platformType, serviceKey, userKey, Collections.emptyMap(), System.currentTimeMillis());
		assertNotNull(issueDto);
		assertEquals(IssueStatus.open, issueDto.getStatus());
		assertEquals(previousTotalCount + 1, issueRepository.count());
	}

	@Test
	@DisplayName("고객 메세지 수신시, open 상태")
	void givenOpenStatus_whenCustomerMessageEvent() throws Exception {

		issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.open)
				.branchId(branch.getId())
				.channel(channel)
				.guest(guest)
				.firstAsked(ZonedDateTime.now())
				.statusModified(ZonedDateTime.now())
				.build());

		// 진행중인 이슈가 있음
		Issue ongoingIssue = issueService.findOngoingByPlatform(channel.getId(), guest.getId());
		assertNotNull(ongoingIssue);
		assertEquals(IssueStatus.open, ongoingIssue.getStatus());

		// ////////////////////////////////////////////////////////////////////
		// 고객 메세지 수신전 상태
		// 전체 메세지 카운트
		long previousTotalCount = issueLogRepository.count();
		// 미답변 메세지 카운트
		long previousAskCount = ongoingIssue.getAskCount();
		// 이슈 상태 변경 시간
		ZonedDateTime statusModified = ongoingIssue.getStatusModified();
		// 미답변 시간
		ZonedDateTime firstAsked = ongoingIssue.getFirstAsked();
		// 이슈 생성 시간
		ZonedDateTime created = ongoingIssue.getCreated();
		// 이슈 변경 시간
		ZonedDateTime modified = ongoingIssue.getModified();

		// ////////////////////////////////////////////////////////////////////
		// 고객 메세지 수신시
		IssueDto issueDto = eventByPlatformService.message(platformType, serviceKey, userKey, IssuePayloadTestFactory.getPayload());
		assertNotNull(issueDto);
		// 이슈 상태 유지
		assertEquals(IssueStatus.open, issueDto.getStatus());
		// 이슈 상태 변경 시간 유지
		assertEquals(statusModified, ongoingIssue.getStatusModified());
		// 메세지 카운트 1 증가
		assertEquals(previousTotalCount + 1, issueLogRepository.count());
		// 미답변 메세지 카운트 1 증가
		assertEquals(previousAskCount + 1, issueDto.getAskCount());
		// 미답변 시간 유지
		assertEquals(firstAsked, ongoingIssue.getFirstAsked());
		// 이슈 생성 시간 유지
		assertEquals(created, issueDto.getCreated());
		// 이슈 변경 시간 변경
		assertTrue(modified.isBefore(issueDto.getModified()));
	}

	@Test
	@DisplayName("고객 메세지 수신시, assign 상태")
	void givenAssignStatus_whenCustomerMessageEvent() throws Exception {

		issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.assign)
				.branchId(branch.getId())
				.channel(channel)
				.guest(guest)
				.firstAsked(ZonedDateTime.now())
				.statusModified(ZonedDateTime.now())
				.build());

		// 진행중인 이슈가 있음
		Issue ongoingIssue = issueService.findOngoingByPlatform(channel.getId(), guest.getId());
		assertNotNull(ongoingIssue);
		assertEquals(IssueStatus.assign, ongoingIssue.getStatus());

		// ////////////////////////////////////////////////////////////////////
		// 고객 메세지 수신전 상태
		// 전체 메세지 카운트
		long previousTotalCount = issueLogRepository.count();
		// 미답변 메세지 카운트
		long previousAskCount = ongoingIssue.getAskCount();
		// 이슈 상태 변경 시간
		ZonedDateTime statusModified = ongoingIssue.getStatusModified();
		// 미답변 시간
		ZonedDateTime firstAsked = ongoingIssue.getFirstAsked();
		// 이슈 생성 시간
		ZonedDateTime created = ongoingIssue.getCreated();
		// 이슈 변경 시간
		ZonedDateTime modified = ongoingIssue.getModified();

		// ////////////////////////////////////////////////////////////////////
		// 고객 메세지 수신시
		IssueDto issueDto = eventByPlatformService.message(platformType, serviceKey, userKey, IssuePayloadTestFactory.getPayload());
		assertNotNull(issueDto);
		// 이슈 상태 유지
		assertEquals(IssueStatus.assign, issueDto.getStatus());
		// 이슈 상태 변경 시간 유지
		assertEquals(statusModified, ongoingIssue.getStatusModified());
		// 전체 메세지 카운트 1 증가
		assertEquals(previousTotalCount + 1, issueLogRepository.count());
		// 미답변 메세지 카운트 1 증가
		assertEquals(previousAskCount + 1, issueDto.getAskCount());
		// 미답변 시간 유지
		assertEquals(firstAsked, ongoingIssue.getFirstAsked());
		// 이슈 생성 시간 유지
		assertEquals(created, issueDto.getCreated());
		// 이슈 변경 시간 변경
		assertTrue(modified.isBefore(issueDto.getModified()));
	}

	@Test
	@DisplayName("고객 메세지 수신시, reply 상태")
	void givenReplyStatus_whenCustomerMessageEvent() throws Exception {

		issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.reply)
				.branchId(branch.getId())
				.channel(channel)
				.guest(guest)
				.firstAsked(ZonedDateTime.now())
				.statusModified(ZonedDateTime.now())
				.build());

		// 진행중인 이슈가 있음
		Issue ongoingIssue = issueService.findOngoingByPlatform(channel.getId(), guest.getId());
		assertNotNull(ongoingIssue);
		assertEquals(IssueStatus.reply, ongoingIssue.getStatus());

		// ////////////////////////////////////////////////////////////////////
		// 고객 메세지 수신전 상태
		// 전체 메세지 카운트
		long previousTotalCount = issueLogRepository.count();
		// 미답변 메세지 카운트
		long previousAskCount = ongoingIssue.getAskCount();
		// 이슈 상태 변경 시간
		ZonedDateTime statusModified = ongoingIssue.getStatusModified();
		// 미답변 시간
		ZonedDateTime firstAsked = ongoingIssue.getFirstAsked();
		// 이슈 생성 시간
		ZonedDateTime created = ongoingIssue.getCreated();
		// 이슈 변경 시간
		ZonedDateTime modified = ongoingIssue.getModified();

		// ////////////////////////////////////////////////////////////////////
		// 고객 메세지 수신시
		IssueDto issueDto = eventByPlatformService.message(platformType, serviceKey, userKey, IssuePayloadTestFactory.getPayload());
		assertNotNull(issueDto);
		// 이슈 상테 변경 (ask)
		assertEquals(IssueStatus.ask, issueDto.getStatus());
		// 이슈 상태 변경 시간 변경
		assertTrue(statusModified.isBefore(ongoingIssue.getStatusModified()));
		// 전체 메세지 카운트 1 증가
		assertEquals(previousTotalCount + 1, issueLogRepository.count());
		// 미답변 메세지 카운트 1 증가
		assertEquals(previousAskCount + 1, issueDto.getAskCount());
		// 미답변 시간 변경
		assertTrue(firstAsked.isBefore(ongoingIssue.getFirstAsked()));
		// 이슈 생성 시간 유지
		assertEquals(created, issueDto.getCreated());
		// 이슈 변경 시간 변경
		assertTrue(modified.isBefore(issueDto.getModified()));
	}

	@Test
	@DisplayName("고객 메세지 수신시, ask 상태")
	void givenAskStatus_whenCustomerMessageEvent() throws Exception {

		issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.ask)
				.branchId(branch.getId())
				.channel(channel)
				.guest(guest)
				.firstAsked(ZonedDateTime.now())
				.statusModified(ZonedDateTime.now())
				.build());

		// 진행중인 이슈가 있음
		Issue ongoingIssue = issueService.findOngoingByPlatform(channel.getId(), guest.getId());
		assertNotNull(ongoingIssue);
		assertEquals(IssueStatus.ask, ongoingIssue.getStatus());

		// ////////////////////////////////////////////////////////////////////
		// 고객 메세지 수신전 상태
		// 전체 메세지 카운트
		long previousTotalCount = issueLogRepository.count();
		// 미답변 메세지 카운트
		long previousAskCount = ongoingIssue.getAskCount();
		// 이슈 상태 변경 시간
		ZonedDateTime statusModified = ongoingIssue.getStatusModified();
		// 미답변 시간
		ZonedDateTime firstAsked = ongoingIssue.getFirstAsked();
		// 이슈 생성 시간
		ZonedDateTime created = ongoingIssue.getCreated();
		// 이슈 변경 시간
		ZonedDateTime modified = ongoingIssue.getModified();

		// ////////////////////////////////////////////////////////////////////
		// 고객 메세지 수신시
		IssueDto issueDto = eventByPlatformService.message(platformType, serviceKey, userKey, IssuePayloadTestFactory.getPayload());
		assertNotNull(issueDto);
		// 이슈 상태 유지
		assertEquals(IssueStatus.ask, issueDto.getStatus());
		// 이슈 상태 변경 시간 유지
		assertEquals(statusModified, ongoingIssue.getStatusModified());
		// 전체 메세지 카운트 1 증가
		assertEquals(previousTotalCount + 1, issueLogRepository.count());
		// 미답변 메세지 카운트 1 증가
		assertEquals(previousAskCount + 1, issueDto.getAskCount());
		// 미답변 시간 유지
		assertEquals(firstAsked, ongoingIssue.getFirstAsked());
		// 이슈 생성 시간 유지
		assertEquals(created, issueDto.getCreated());
		// 이슈 변경 시간 변경
		assertTrue(modified.isBefore(issueDto.getModified()));
	}

	@Test
	@DisplayName("고객 메세지 수신시, close 상태")
	void givenCloseStatus_whenCustomerMessageEvent() throws Exception {

		Issue issue = issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.close)
				.branchId(branch.getId())
				.channel(channel)
				.guest(guest)
				.firstAsked(ZonedDateTime.now())
				.statusModified(ZonedDateTime.now())
				.build());

		// 진행중인 이슈가 없음
		Issue ongoingIssue = issueService.findOngoingByPlatform(channel.getId(), guest.getId());
		assertNull(ongoingIssue);

		// ////////////////////////////////////////////////////////////////////
		// 고객 메세지 수신전 상태
		// 직렬화 결과
		String previousIssueJson = objectMapper.writeValueAsString(issue);
		// 전체 메세지 카운트
		long previousTotalCount = issueLogRepository.count();
		// 미답변 메세지 카운트
		long previousAskCount = issue.getAskCount();
		// 이슈 상태 변경 시간
		ZonedDateTime statusModified = issue.getStatusModified();
		// 미답변 시간
		ZonedDateTime firstAsked = issue.getFirstAsked();
		// 이슈 생성 시간
		ZonedDateTime created = issue.getCreated();
		// 이슈 변경 시간
		ZonedDateTime modified = issue.getModified();

		// ////////////////////////////////////////////////////////////////////
		// 고객 메세지 수신시, IllegalArgumentException 발생
		assertThrows(IllegalArgumentException.class, () -> {
			eventByPlatformService.message(platformType, serviceKey, userKey, IssuePayloadTestFactory.getPayload());
		});
		// 직렬화 결과가 같아야 함
		assertEquals(previousIssueJson, objectMapper.writeValueAsString(issue));
		// 전체 메세지 카운트 유지
		assertEquals(previousTotalCount, issueLogRepository.count());
		// 이슈 상태 유지
		assertEquals(IssueStatus.close, issue.getStatus());
		// 이슈 상태 변경 시간 유지
		assertEquals(statusModified, issue.getStatusModified());
		// 미답변 메세지 카운트 유지
		assertEquals(previousAskCount, issue.getAskCount());
		// 미답변 시간 유지
		assertEquals(firstAsked, issue.getFirstAsked());
		// 이슈 생성 시간 유지
		assertEquals(created, issue.getCreated());
		// 이슈 변경 시간 유지
		assertEquals(modified, issue.getModified());
	}
}
