package com.kep.portal.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.channel.ChannelDto;
import com.kep.core.model.dto.customer.GuestDto;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.IssueLogDto;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.portal.config.client.InternalServiceClientConfig;
import com.kep.portal.config.property.CoreProperty;
import com.kep.portal.config.property.PlatformProperty;
import com.kep.portal.model.dto.IssuePayloadTestFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@ExtendWith(SpringExtension.class)
@RestClientTest({PlatformClient.class})
@Slf4j
class PlatformClientMockTest {

	@TestConfiguration
	static class ContextConfig {
		@Bean
		public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
			return new InternalServiceClientConfig().restTemplate(restTemplateBuilder);
		}
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapperConfig().objectMapper();
		}
	}

	@Resource
	private PlatformClient platformClient;
	@Resource
	private CoreProperty coreProperty;
	@Resource
	private PlatformProperty platformProperty;
	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private MockRestServiceServer mockServer;

	private IssueDto issueDto;

	@BeforeEach
	void beforeEach() throws Exception {

		issueDto = IssueDto.builder()
				.id(Math.abs(new Random().nextLong()))
				.status(IssueStatus.reply)
				.branchId(new Random().nextLong())
				.channel(ChannelDto.builder()
						.platform(PlatformType.kakao_counsel_talk)
						.serviceKey("TEST_SERVICE_KEY")
						.build())
				.guest(GuestDto.builder()
						.userKey("TEST_USER_KEY")
						.build())
				.build();
	}

	@Test
	@DisplayName("message 이벤트")
	void testMessage() throws Exception {

		Long issueLogId = Math.abs(new Random().nextLong());
		IssuePayload issuePayload = IssuePayloadTestFactory.getPayload();

		// Mock Server 에서 받을 payload
		List<IssuePayload> issuePayloads = new ArrayList<>();
		issuePayloads.add(issuePayload);
		String payload = objectMapper.writeValueAsString(issuePayloads);

		// Mock Server, 실제 연동 테스트하려면 주석처리
		String baseUrl = coreProperty.getPlatformServiceUri() + platformProperty.getApiBasePath();
		String requestUrl = baseUrl + platformProperty.getMessagePath();
		mockServer
				.expect(requestTo(requestUrl))
				.andExpect(method(HttpMethod.POST))
				.andExpect(header("X-Platform-Type", PlatformType.kakao_counsel_talk.name()))
				.andExpect(header("X-Service-Key", "TEST_SERVICE_KEY"))
				.andExpect(header("X-User-Key", "TEST_USER_KEY"))
				.andExpect(header("X-Event-Key", "" + issueLogId))
				.andExpect(content().json(payload))
//				.andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON))
				.andRespond(withStatus(HttpStatus.CREATED));

		// message 이벤트 테스트
		IssueLogDto issueLogDto = IssueLogDto.builder()
				.id(issueLogId)
				.payload(objectMapper.writeValueAsString(issuePayload))
				.build();
		boolean result = platformClient.message(issueDto, issueLogDto);
		assertTrue(result);
	}
}
