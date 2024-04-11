package com.kep.portal.model.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.issue.IssueLogDto;
import com.kep.portal.model.dto.IssuePayloadTestFactory;
import com.kep.portal.model.entity.issue.IssueLog;
import com.kep.portal.model.entity.issue.IssueLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.TypePredicates;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class IssueLogMapperTest {

	@Resource
	private IssueLogMapper issueLogMapper;
	@Resource
	private ObjectMapper objectMapper;
	private EasyRandom generator;

	@BeforeEach
	void beforeEach() throws Exception {

		EasyRandomParameters parameters = new EasyRandomParameters();
		parameters.stringLengthRange(1, 30);
		parameters.collectionSizeRange(1, 1);
		parameters.excludeType(TypePredicates.ofType(Object.class));
		generator = new EasyRandom(parameters);
	}

	@Test
	@DisplayName("DTO 변환시, 데이터 보존")
	void givenDTO_whenMap_thenNotNullAndEqualData() throws Exception {

		IssueLogDto issueLogDto = generator.nextObject(IssueLogDto.class);

		IssueLog issueLog = issueLogMapper.map(issueLogDto);
		assertNotNull(issueLog);
		assertEquals(issueLogDto.getId(), issueLog.getId());
	}

	@Test
	@DisplayName("엔터티 변환시, 데이터 보존")
	void givenEntity_whenMap_thenNotNullAndEqualData() throws Exception {

		IssueLog issueLog = generator.nextObject(IssueLog.class);
		issueLog.setPayload(objectMapper.writeValueAsString(IssuePayloadTestFactory.getMap(objectMapper)));
		log.debug("issueLog: {}", objectMapper.writeValueAsString(issueLog));

		IssueLogDto issueLogDto = issueLogMapper.map(issueLog);
		log.debug("issueLogDto: {}", objectMapper.writeValueAsString(issueLogDto));
		assertEquals(objectMapper.writeValueAsString(issueLog.getPayload()), objectMapper.writeValueAsString(issueLogDto.getPayload()));
	}

	@Test
	@DisplayName("NULL 변환시, NULL")
	void givenMull_whenMap_thenNull() throws Exception {

		IssueLog issueLog = null;
		IssueLogDto issueLogDto = issueLogMapper.map(issueLog);
		assertNull(issueLogDto);
	}
}
