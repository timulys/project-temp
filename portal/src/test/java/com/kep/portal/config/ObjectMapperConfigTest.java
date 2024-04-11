package com.kep.portal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.issue.IssueLogDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.issue.IssueLogStatus;
import com.kep.portal.model.dto.IssuePayloadTestFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Slf4j
class ObjectMapperConfigTest {

	@TestConfiguration
	static class ContextConfig {
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapperConfig().objectMapper();
		}
	}

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private ResourceLoader resourceLoader;

	@Test
	public void testDeserialize() throws Exception {


		org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:config/ObjectMapperConfigTest_issueLogJson.json");
		String issueLogJson = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
		IssueLogDto issueLogDto = objectMapper.readValue(issueLogJson, IssueLogDto.class);
		assertNotNull(issueLogDto.getCreator());
		assertEquals(749010L, issueLogDto.getCreator());
	}

	@Test
	void testSerialize() throws Exception {

		IssuePayload issuePayload = IssuePayloadTestFactory.getPayload();
		IssueLogDto issueLogDto = IssueLogDto.builder()
				.id(new Random().nextLong())
				.issueId(new Random().nextLong())
				.status(IssueLogStatus.send)
				.relativeId(new Random().nextLong())
				.payload(objectMapper.writeValueAsString(issuePayload))
				.creator(new Random().nextLong())
				.created(ZonedDateTime.now())
				.build();
		String issueLogJson = objectMapper.writeValueAsString(issueLogDto);
		assertFalse(ObjectUtils.isEmpty(issueLogJson));
		log.info("issueLogJson: {}", issueLogJson);
	}
}
