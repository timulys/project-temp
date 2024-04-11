package com.kep.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.platform.config.ObjectMapperConfig;
import com.kep.platform.model.dto.KakaoCounselReceiveRelayBotEvent;
import com.kep.platform.service.kakao.counsel.KakaoCounselParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@ExtendWith(SpringExtension.class)
@Slf4j
public class KakaoCounselParserTests {

	@TestConfiguration
	public static class ContextConfig {
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapperConfig().objectMapper();
		}
		@Bean
		public KakaoCounselParser kakaoCounselParser() {
			return new KakaoCounselParser();
		}
		@Bean
		public ResourceLoader resourceLoader() {
			return new DefaultResourceLoader();
		}
	}

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private KakaoCounselParser kakaoCounselParser;
	@Resource
	private ResourceLoader resourceLoader;

	@Test
	public void parseRelayBotMessage() throws Exception {

		org.springframework.core.io.Resource file = resourceLoader.getResource("classpath:kakao/openbuilder/OpenBuilderBlockButton2.json");
		String contents = new String(Files.readAllBytes(file.getFile().toPath()), StandardCharsets.UTF_8);
		KakaoCounselReceiveRelayBotEvent event = objectMapper.readValue(contents, KakaoCounselReceiveRelayBotEvent.class);

		IssuePayload issuePayload = kakaoCounselParser.parseRelayAsk(event);
		log.info("ask: {}", issuePayload);
		List<IssuePayload> issuePayloads = kakaoCounselParser.parseRelayReply(event);
		log.info("reply: {}", issuePayloads);
	}

	@Test
	public void replaceImageUrl() throws Exception {

		String originalImageUrl = "http://dn-m.talk.kakao.com/talkm/oXBz1wmtKx/30o9iqVWyBGKCYscWSdcO0/i_kaon4mjnv0fv.jpeg";
		originalImageUrl = originalImageUrl.replace("dn-m.talk.kakao.com", "dn-m.talk.kakao.com:8301");

		log.info("originalImageUrl: {}", originalImageUrl);
	}
}
