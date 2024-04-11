package com.kep.portal.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.channel.ChannelDto;
import com.kep.core.model.dto.customer.GuestDto;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.IssueLogDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.platform.PlatformType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class PlatformClientTest {

	@Resource
	private PlatformClient platformClient;
	@Resource
	private ObjectMapper objectMapper;

	@Test
	@Disabled("Platform 서비스 실행 필요")
	void testBotHistory() throws Exception {

		ChannelDto channel = ChannelDto.builder()
				.platform(PlatformType.kakao_counsel_talk)
				.serviceKey("adcbd6de27772a3660b4a3a610a1e837262e2341")
				.build();
		GuestDto guest = GuestDto.builder()
				.userKey("eZrqKKrX1-1o")
				.build();
		IssueDto issue = IssueDto.builder()
				.channel(channel)
				.guest(guest)
				.build();

		try {
			List<IssuePayload> issuePayloads = platformClient.botHistory(issue);
			assertNotNull(issuePayloads);
			log.info("{}", objectMapper.writeValueAsString(issuePayloads));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
}
