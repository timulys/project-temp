package com.kep.portal.service.issue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueType;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.QIssue;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.repository.channel.ChannelRepository;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.repository.issue.IssueRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class IssueServiceTest {

	@Resource
	private IssueService issueService;
	@Resource
	private IssueRepository issueRepository;
	@Resource
	private GuestRepository guestRepository;
	@Resource
	private ChannelRepository channelRepository;
	@PersistenceContext
	private EntityManager entityManager;
	@Resource
	private ObjectMapper objectMapper;

	private Channel channel;
	private Guest guest;
	private Issue issue;

	@BeforeEach
	void beforeEach() {

		channel = channelRepository.save(Channel.builder()
				.name("UNIT_TEST_CHANNEL")
				.platform(PlatformType.kakao_counsel_talk)
				.serviceId("TEST_SERVICE_ID")
				.serviceKey("TEST_SERVICE_KEY")
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		channelRepository.flush();
		guest = guestRepository.save(Guest.builder()
				.channelId(channel.getId())
				.userKey("UNIT_TEST_USER_KEY")
				.created(ZonedDateTime.now())
				.build());
		guestRepository.flush();
		issue = issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.open)
				.branchId(1L)
				.channel(channel)
				.guest(guest)
				.build());
		issueRepository.flush();
	}

	@Test
	@DisplayName("한 상담톡에 두 개의 이슈가 열려있을 경우, 최근 이슈를 리턴")
	void findOngoingByPlatform() {

		Issue duplicationIssue = issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.open)
				.branchId(1L)
				.channel(channel)
				.guest(guest)
				.build());
		issueRepository.flush();

		Issue ongoingIssue = issueService.findOngoingByPlatform(channel.getId(), guest.getId());
		assertNotNull(ongoingIssue);
		assertEquals(duplicationIssue.getId(), ongoingIssue.getId());
		assertTrue(duplicationIssue.getModified().isAfter(issue.getModified()));
	}

	@Test
	void search() throws Exception {

		List<IssueStatus> statuses = new ArrayList<>();
		statuses.add(IssueStatus.open);
//		statuses.add(IssueStatus.relay);

		List<Long> memberIds = new ArrayList<>();
		memberIds.add(1L);
		memberIds.add(2L);
		List<Member> members = memberIds.stream().map(o -> Member.builder().id(o).build()).collect(Collectors.toList());

		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QIssue entity = new QIssue("issue");

		List<Issue> issues = queryFactory.selectFrom(entity)
				.where(entity.status.in(statuses)
						.and(entity.member.in(members))
						.and(entity.teamId.eq(1L)))
				.fetch();

		log.info(objectMapper.writeValueAsString(issues));
	}
}
