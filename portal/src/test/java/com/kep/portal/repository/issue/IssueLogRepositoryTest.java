package com.kep.portal.repository.issue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.portal.config.QueryDslConfig;
import com.kep.portal.model.dto.IssuePayloadTestFactory;
import com.kep.portal.model.entity.issue.IssueLog;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.TypePredicates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Slf4j
class IssueLogRepositoryTest {

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
	}

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private IssueLogRepository issueLogRepository;
	@Resource
	private EntityManager entityManager;

	private final int MAX_ENTITY = 10;
	private final Long issueId = 12L;
	private EasyRandom generator;

	@BeforeEach
	void beforeEach() throws Exception {

		EasyRandomParameters parameters = new EasyRandomParameters();
		parameters.stringLengthRange(1, 30);
		parameters.collectionSizeRange(1, 1);
		parameters.excludeType(TypePredicates.ofType(Object.class));
		generator = new EasyRandom(parameters);

		for (int i = 0; i < MAX_ENTITY; i++) {

			IssueLog issueLog = generator.nextObject(IssueLog.class);
			issueLog.setIssueId(issueId);
			issueLog.setCreator(1L);
			issueLog.setCreated(ZonedDateTime.now());
			issueLog.setRelativeId(1L);
			issueLog.setAssigner(null);
			issueLog.setPayload(objectMapper.writeValueAsString(IssuePayloadTestFactory.getMap(objectMapper)));

			issueLogRepository.save(issueLog);
		}

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	void testFactory() {

		for (int i = 0; i < 100; i++) {
			IssuePayload issuePayload = IssuePayloadTestFactory.getPayload();
			log.info("issuePayload: {}", IssuePayloadTestFactory.getPayload());

			assertNotNull(issuePayload);

			assertFalse(ObjectUtils.isEmpty(issuePayload.getChapters()));

			IssuePayload.Chapter chapter = issuePayload.getChapters().get(0);
			assertNotNull(chapter);
			assertFalse(ObjectUtils.isEmpty(chapter.getSections()));

			IssuePayload.Section section = chapter.getSections().get(0);
			assertNotNull(section);
			assertNotNull(section.getType());
		}
	}

	@Test
	void testSave() throws Exception {

		IssueLog issueLog = generator.nextObject(IssueLog.class);
		issueLog.setPayload(objectMapper.writeValueAsString(IssuePayloadTestFactory.getMap(objectMapper)));
		log.info("issueLog: {}", objectMapper.writeValueAsString(issueLog));

		IssueLog issueLogEntity = issueLogRepository.save(issueLog);
		log.info("issueLogEntity: {}", objectMapper.writeValueAsString(issueLogEntity));

		assertEquals(issueLog.getPayload(), issueLogEntity.getPayload());
	}

	@Test
	void testFind() throws Exception {

		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("created")));
		Page<IssueLog> page = issueLogRepository.findAll(Example.of(IssueLog.builder()
						.issueId(issueId)
				.build()), pageRequest);
		log.info("PAGE: {}", objectMapper.writeValueAsString(page));
		assertEquals(MAX_ENTITY, page.getTotalElements());
	}

	@Test
	void testCountByIssueIdAndCreatorAndCreatedAfter() throws Exception {

		long count = issueLogRepository.countByIssueIdAndCreatorAndCreatedAfter(issueId, 1L, ZonedDateTime.now());
		assertEquals(0, count);
	}
}
