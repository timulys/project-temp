package com.kep.portal.repository.branch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.portal.config.QueryDslConfig;
import com.kep.portal.model.entity.branch.Branch;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Random;

@DataJpaTest
@Slf4j
class BranchRepositoryTest {

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
	private BranchRepository branchRepository;
	@Resource
	private EntityManager entityManager;

	private Long branchId;

	@BeforeEach
	void beforeEach() throws Exception {

		Branch branch = branchRepository.save(Branch.builder()
				.name("VIP")
				.assign(WorkType.Cases.branch)
				.offDutyHours(false)
				.enabled(true)
				.headQuarters(false)
				.creator(1L)
				.status(WorkType.OfficeHoursStatusType.on)
				.created(ZonedDateTime.now())
				.build());

		branchRepository.flush();
		entityManager.clear();

		branchId = branch.getId();


	}

	@Test
	void givenNewPOJO_whenSave() throws Exception {

		Branch branch = Branch.builder()
				.id(Math.abs(new Random().nextLong()))
				.name("LOAN")
				.assign(WorkType.Cases.branch)
				.offDutyHours(false)
				.enabled(true)
				.headQuarters(false)
				.creator(1L)
				.created(ZonedDateTime.now())
				.build();
		branch = branchRepository.save(branch);
		branchRepository.flush();
		log.info("{}", objectMapper.writeValueAsString(branch));
	}

	@Test
	void givenExistEntity_whenSave() throws Exception {

		Optional<Branch> optional = branchRepository.findById(branchId);
		if (optional.isPresent()) {
			Branch branch = optional.get();
			branch.setEnabled(false);
			branch.setModifier(1L);
			branch.setModified(ZonedDateTime.now());
			branchRepository.save(branch);
			branchRepository.flush();
			log.info("{}", objectMapper.writeValueAsString(branch));
		}
	}
}
