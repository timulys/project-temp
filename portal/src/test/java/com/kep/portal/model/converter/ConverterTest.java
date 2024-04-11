package com.kep.portal.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.portal.config.QueryDslConfig;
import com.kep.portal.model.dto.IssuePayloadTestFactory;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.TypePredicates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // non-static @BeforeAll
@Slf4j
class ConverterTest {

	@TestConfiguration
	static class ContextConfig {
		@Bean
		public JPAQueryFactory queryFactory(EntityManager entityManager) {
			return new QueryDslConfig().jpaQueryFactory(entityManager);
		}
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapperConfig().objectMapper();
		}
		@Bean
		public BooleanConverter booleanConverter() {
			return new BooleanConverter();
		}
		@Bean
		public MapConverter mapConverter() {
			return new MapConverter();
		}
	}

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private BooleanConverter booleanConverter;
	@Resource
	private MapConverter maoConverter;
	@Resource
	private MemberRepository memberRepository;

	private Member memberEntity;

	@BeforeEach
	void beforeEach() throws Exception {

		Member member = Member.builder()
				.branchId(1L)
				.enabled(false)
				.username("tester")
				.nickname("TESTER")
				.password("TESTER PASSWORD")
				.build();
		log.info("member: {}", objectMapper.writeValueAsString(member));

		memberEntity = memberRepository.save(member);
		assertFalse(memberEntity.getEnabled());
	}

	@Test
	void testBooleanConverter() {

		Boolean attribute = true;
		String dbData = booleanConverter.convertToDatabaseColumn(attribute);
		assertEquals("Y", dbData);

		dbData = "Y";
		attribute = booleanConverter.convertToEntityAttribute(dbData);
		assertTrue(attribute);

		dbData = "A";
		attribute = booleanConverter.convertToEntityAttribute(dbData);
		assertFalse(attribute);
	}

	@Test
	void testSaveEntity() {

		assertFalse(memberEntity.getEnabled());
		memberEntity.setEnabled(true);
		memberEntity = memberRepository.save(memberEntity);
		assertTrue(memberEntity.getEnabled());
	}

	@Test
	void testMapConverter() throws Exception {

		EasyRandomParameters parameters = new EasyRandomParameters();
		parameters.stringLengthRange(1, 30);
		parameters.collectionSizeRange(2, 5);
		parameters.excludeType(TypePredicates.ofType(Object.class));
		EasyRandom generator = new EasyRandom(parameters);

		IssuePayload issuePayload = IssuePayloadTestFactory.getPayload();
		String json = objectMapper.writeValueAsString(issuePayload);
		assertNotNull(json);
		log.info("json: {}", json);
		Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});

		String dbData = maoConverter.convertToDatabaseColumn(map);
		assertNotNull(dbData);
		log.info("dbData: {}", dbData);
	}
}
