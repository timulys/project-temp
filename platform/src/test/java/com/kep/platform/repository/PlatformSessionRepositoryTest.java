package com.kep.platform.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.platform.model.entity.PlatformSession;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // non-static @BeforeAll
@Slf4j
class PlatformSessionRepositoryTest {

	@Resource
	private PlatformSessionRepository platformSessionRepository;
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	@Resource
	private ObjectMapper objectMapper;

	private final String id = PlatformType.kakao_counsel_talk.name() + "_" + "TEST_SERVICE_KEY" + "_" + "TEST_USER_KEY";
	private final Long issueId = 1000L;

	@BeforeAll
	void beforeAll() {
		for (int i = 0; i < 10; i++) {
			PlatformSession platformSession = PlatformSession.builder()
					.id(id + i)
					.issueId(issueId)
					.platformType(PlatformType.kakao_counsel_talk)
					.serviceKey("TEST_SERVICE_KEY")
					.userKey("TEST_USER_KEY")
					.created(ZonedDateTime.now())
					.build();
			platformSessionRepository.save(platformSession);
		}
	}

	@Test
	void testFindById() throws Exception {

		PlatformSession platformSession = platformSessionRepository.findById(id + 0).orElse(null);
		assertNotNull(platformSession);
		assertEquals(issueId, platformSession.getIssueId());
	}

	@Test
	void testDeleteById() throws Exception {

		platformSessionRepository.deleteById(id);
		PlatformSession platformSession = platformSessionRepository.findById(id).orElse(null);
		assertNull(platformSession);
	}

	@Test
	void testSave() throws Exception {

		PlatformSession platformSession = platformSessionRepository.findById(id + 0).orElse(null);
		if (platformSession != null) {
			TimeUnit.SECONDS.sleep(2);
			platformSession.setCreated(ZonedDateTime.now());
			platformSession = platformSessionRepository.save(platformSession);
		}
	}
}
