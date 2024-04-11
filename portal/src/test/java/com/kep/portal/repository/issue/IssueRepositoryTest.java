package com.kep.portal.repository.issue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.portal.config.JasyptConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueType;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.portal.config.QueryDslConfig;
import com.kep.portal.model.dto.issue.IssueSearchCondition;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.subject.IssueCategory;
import com.kep.portal.repository.customer.CustomerRepository;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.repository.channel.ChannelRepository;
import com.kep.portal.repository.subject.IssueCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Slf4j
class IssueRepositoryTest {

	@TestConfiguration
	public static class ContextConfig {
		@Bean
		public JPAQueryFactory queryFactory(EntityManager entityManager) {
			return new QueryDslConfig().jpaQueryFactory(entityManager);
		}
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapperConfig().objectMapper();
		}
		@Bean(name = "fixedEncryptor")
		public StringEncryptor fixedEncryptor() {
			return new JasyptConfig().fixedEncryptor();
		}
	}

	@Resource
	private IssueRepository issueRepository;

	@Resource
	private ChannelRepository channelRepository;
	@Resource
	private IssueCategoryRepository issueCategoryRepository;
	@Resource
	private CustomerRepository customerRepository;
	@Resource
	private GuestRepository guestRepository;
	@Resource
	private EntityManager entityManager;
	@Resource
	private JPAQueryFactory queryFactory;
	@Resource
	private ObjectMapper objectMapper;

	private final EasyRandom generator = new EasyRandom();
	private Long issueId;
	private Channel channel;
	private Guest guest1;
	private Guest guest2;
	private Long issueCategoryId;
	private Customer customer1;
	private Customer customer2;

	@BeforeEach
	void beforeEach() throws Exception {

		channel = channelRepository.save(Channel.builder()
				.name("test channel")
				.platform(PlatformType.solution_web)
				.serviceId("@service_id")
				.serviceKey("service_key")
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());

		Guest guest = guestRepository.save(Guest.builder()
				.channelId(channel.getId())
				.userKey("user_key")
				.created(ZonedDateTime.now())
				.build());

		Issue issue = Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.open)
				.branchId(1L)
				.channel(channel)
				.guest(guest)
				.build();

		Issue issueEntity = issueRepository.save(issue);
		issueId = issueEntity.getId();

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	void testFindById() throws Exception {

		Optional<Issue> issueOptional = issueRepository.findById(issueId);
		assertTrue(issueOptional.isPresent());
		assertEquals(issueId, issueOptional.get().getId());
	}

	private void initSearch() {

		customer1 = customerRepository.save(Customer.builder()
				.name("김대출")
				.build());
		guest1 = guestRepository.save(Guest.builder()
				.channelId(channel.getId())
				.userKey("user_key_1")
				.customer(customer1)
				.created(ZonedDateTime.now())
				.build());

		customer2 = customerRepository.save(Customer.builder()
				.name("박대출")
				.build());
		guest2 = guestRepository.save(Guest.builder()
				.channelId(channel.getId())
				.userKey("user_key_2")
				.customer(customer2)
				.created(ZonedDateTime.now())
				.build());

		IssueCategory issueCategory1 = issueCategoryRepository.save(IssueCategory.builder()
				.name("직장인대출")
				.channelId(channel.getId())
				.depth(3)
				.sort(1)
				.enabled(true)
				.exposed(true)
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		issueCategoryId = issueCategory1.getId();

		Issue issue1 = issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.open)
				.branchId(1L)
				.channel(channel)
				.issueCategory(issueCategory1)
				.guest(guest1)
				.customerId(customer1.getId())
				.build());
		Issue issue2 = issueRepository.save(Issue.builder()
				.type(IssueType.chat)
				.status(IssueStatus.open)
				.branchId(1L)
				.channel(channel)
				.issueCategory(issueCategory1)
				.guest(guest2)
				.customerId(customer2.getId())
				.build());

		for (int i = 0; i < 100; i++) {
			issueRepository.save(Issue.builder()
					.type(IssueType.chat)
					.status(IssueStatus.open)
					.branchId(1L)
					.channel(channel)
					.issueCategory(issueCategory1)
					.guest(guest2)
					.customerId(customer2.getId())
					.build());
		}

		entityManager.flush();
		entityManager.clear();

		assertEquals(103L, issueRepository.count());
	}

	@Test
	void testSearch() throws Exception {

		initSearch();

		IssueSearchCondition condition = IssueSearchCondition.builder()
				.categoryId(issueCategoryId)
				.status(Arrays.asList(IssueStatus.open, IssueStatus.assign, IssueStatus.ask, IssueStatus.reply))
				.guests(Arrays.asList(guest1, guest2))
				.build();
		Page<Issue> issuePage = issueRepository.search(condition, PageRequest.of(0, 1000));
		log.info(objectMapper.writeValueAsString(issuePage));
	}

	@Test
	void testHasNext() throws Exception {

		initSearch();

		final int pageSize = 2;
		int page = 0;
		int totalElement = 0;
		while (true) {
			Pageable pageable = PageRequest.of(page++, pageSize, Sort.Direction.ASC, "created");
			Slice<Issue> issuePage = issueRepository.findAll(pageable);
			assertFalse(issuePage.isEmpty());
			assertTrue(issuePage.getNumberOfElements() <= 2);
			totalElement += issuePage.getNumberOfElements();
			log.warn("issuePage: {}", issuePage.getContent().stream().map(Issue::getId).collect(Collectors.toList()));
			if (!issuePage.hasNext()) {
				break;
			}
		}

		assertEquals(issueRepository.count(), totalElement);
	}
}
