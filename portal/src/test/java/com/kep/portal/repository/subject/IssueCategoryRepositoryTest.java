package com.kep.portal.repository.subject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.portal.config.QueryDslConfig;
import com.kep.portal.model.entity.subject.IssueCategory;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Slf4j
class IssueCategoryRepositoryTest {

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
	private IssueCategoryRepository issueCategoryRepository;
	@Resource
	private EntityManager entityManager;

	private final EasyRandom generator = new EasyRandom();
	private Long childId;
	private Long parentId;
	private Long grandParentId;

	@BeforeEach
	void beforeEach() throws Exception {

		for (int i = 0; i < 10; i++) {
			log.info("{}", (i + 1) % 4);
			IssueCategory issueCategory = IssueCategory.builder()
					.name(generator.nextObject(String.class))
//					.branchId(Math.abs(new Random().nextLong()))
					.channelId(1L)
					.modifier(1L)
					.modified(ZonedDateTime.now())
					.depth(i % 4 + 1)
					.sort(i + 1)
					.enabled(true)
					.exposed(true)
					.build();
			issueCategory = issueCategoryRepository.save(issueCategory);
			if (i == 8) childId = issueCategory.getId();
			if (i == 5) parentId = issueCategory.getId();
			if (i == 2) grandParentId = issueCategory.getId();
		}

		IssueCategory child = issueCategoryRepository.findById(childId).orElse(null);
		assertNotNull(child);
		IssueCategory parent = issueCategoryRepository.findById(parentId).orElse(null);
		assertNotNull(parent);
		IssueCategory grandParent = issueCategoryRepository.findById(grandParentId).orElse(null);
		assertNotNull(grandParent);

		child.setParent(parent);
		issueCategoryRepository.save(child);
		parent.setParent(grandParent);
		issueCategoryRepository.save(parent);

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	void testFindById() {

		IssueCategory child = issueCategoryRepository.findById(childId).orElse(null);
		assertNotNull(child);
		assertNotNull(child.getParent());
		assertNotNull(child.getParent().getParent());
		assertEquals(grandParentId, child.getParent().getParent().getId());
	}

	@Test
	void testFindByParentId() {

		IssueCategory grandParent = issueCategoryRepository.findById(grandParentId).orElse(null);
		assertNotNull(grandParent);
		List<IssueCategory> issueCategories = issueCategoryRepository.findAll(Example.of(IssueCategory.builder()
				.parent(grandParent).build()));
		assertNotNull(issueCategories);
		assertFalse(issueCategories.isEmpty());
	}
}
