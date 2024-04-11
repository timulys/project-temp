package com.kep.portal.repository.issue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.portal.config.QueryDslConfig;
import com.kep.portal.model.entity.issue.IssueExtra;
import com.kep.portal.model.entity.issue.IssueExtraMemo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
class IssueExtraRepositoryTest {

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
	private IssueExtraRepository issueExtraRepository;

	@BeforeEach
	void beforeEach() {

		issueExtraRepository.save(IssueExtra.builder()
				.guestId(1L)
				.inflow("TEST_INFLOW")
				.memo("TEST_MEMO")
				.memoModified(ZonedDateTime.now())
				.build());
		issueExtraRepository.flush();
	}

	@Test
	void projection() throws Exception {

		Page<IssueExtraMemo> page = issueExtraRepository.findAllByGuestIdAndMemoNotNull(1L, PageRequest.of(0, 10));
		log.info(objectMapper.writeValueAsString(page));
	}
}
