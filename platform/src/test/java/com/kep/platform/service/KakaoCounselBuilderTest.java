package com.kep.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import com.kep.platform.config.ObjectMapperConfig;
import com.kep.platform.model.dto.KakaoCounselSendEvent;
import com.kep.platform.service.kakao.counsel.KakaoCounselBuilder;
import com.kep.platform.service.kakao.counsel.KakaoCounselService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@Slf4j
class KakaoCounselBuilderTest {

	@TestConfiguration
	public static class ContextConfig {
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapperConfig().objectMapper();
		}
		@Bean
		public KakaoCounselBuilder kakaoCounselBuilder() {
			return new KakaoCounselBuilder();
		}
	}

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private KakaoCounselBuilder kakaoCounselBuilder;
	@Resource
	private ResourceLoader resourceLoader;
	@MockBean
	private KakaoCounselService kakaoCounselService;

	@BeforeEach
	void beforeEach() throws Exception {

		given(kakaoCounselService.uploadImage(any(UploadPlatformRequestDto.class), anyString()))
				.willReturn("https://wherever.com/some/image.png");
		given(kakaoCounselService.uploadImage(any(UploadPlatformRequestDto.class), anyString(), anyLong()))
				.willReturn("https://wherever.com/some/image.png");
	}

	@DisplayName("텍스트 메세지 빌드")
	@Test
	public void buildTextMessage() throws Exception {

		org.springframework.core.io.Resource portalFormatFile = resourceLoader.getResource("classpath:portal/text.json");
		String portalJson = new String(Files.readAllBytes(portalFormatFile.getFile().toPath()), StandardCharsets.UTF_8);
		IssuePayload issuePayload = objectMapper.readValue(portalJson, IssuePayload.class);

		List<KakaoCounselSendEvent> events = kakaoCounselBuilder.build(
				issuePayload, "serviceKey", "userKey", "" + System.currentTimeMillis());

		assertNotNull(events);
		assertFalse(events.isEmpty());
		assertEquals(1, events.size());
		KakaoCounselSendEvent event = events.get(0);
		assertEquals(KakaoCounselSendEvent.MESSAGE_TYPE_TEXT, event.getMessageType());
		assertNotNull(event.getMessage());
	}

	@DisplayName("이미지 메세지 빌드, 일반 타입 (not 링크 타입) 이미지 필요")
	@Test
	public void buildImageMessage() throws Exception {

		org.springframework.core.io.Resource portalFormatFile = resourceLoader.getResource("classpath:portal/image.json");
		String portalJson = new String(Files.readAllBytes(portalFormatFile.getFile().toPath()), StandardCharsets.UTF_8);
		IssuePayload issuePayload = objectMapper.readValue(portalJson, IssuePayload.class);

		List<KakaoCounselSendEvent> events = kakaoCounselBuilder.build(
				issuePayload, "serviceKey", "userKey", "" + System.currentTimeMillis());

		assertNotNull(events);
		assertFalse(events.isEmpty());
		assertEquals(1, events.size());
		KakaoCounselSendEvent event = events.get(0);
		assertEquals(KakaoCounselSendEvent.MESSAGE_TYPE_IMAGE, event.getMessageType());
		assertNotNull(event.getImageUrl());
	}

	@DisplayName("이미지, 텍스트 메세지 빌드, 링크 타입 이미지 필요")
	@Test
	public void buildImageTextMessage() throws Exception {

		org.springframework.core.io.Resource portalFormatFile = resourceLoader.getResource("classpath:portal/image_text.json");
		String portalJson = new String(Files.readAllBytes(portalFormatFile.getFile().toPath()), StandardCharsets.UTF_8);
		IssuePayload issuePayload = objectMapper.readValue(portalJson, IssuePayload.class);

		List<KakaoCounselSendEvent> events = kakaoCounselBuilder.build(
				issuePayload, "serviceKey", "userKey", "" + System.currentTimeMillis());

		assertNotNull(events);
		assertFalse(events.isEmpty());
		assertEquals(1, events.size());
		KakaoCounselSendEvent event = events.get(0);
		assertEquals(KakaoCounselSendEvent.MESSAGE_TYPE_LINK, event.getMessageType());
		assertNotNull(event.getMessage());
		assertNotNull(event.getImageUrl());
	}

	@DisplayName("플랫폼 메세지")
	@Test
	public void buildPlatformMessage() throws Exception {

		org.springframework.core.io.Resource portalFormatFile = resourceLoader.getResource("classpath:portal/platform_answer.json");
		String portalJson = new String(Files.readAllBytes(portalFormatFile.getFile().toPath()), StandardCharsets.UTF_8);
		IssuePayload issuePayload = objectMapper.readValue(portalJson, IssuePayload.class);

		List<KakaoCounselSendEvent> events = kakaoCounselBuilder.build(
				issuePayload, "serviceKey", "userKey", "" + System.currentTimeMillis());

		assertNotNull(events);
		assertFalse(events.isEmpty());
		assertEquals(1, events.size());
		KakaoCounselSendEvent event = events.get(0);
		assertEquals(KakaoCounselSendEvent.MESSAGE_TYPE_LINK, event.getMessageType());
		assertNull(event.getMessage());
		assertNotNull(event.getAutoAnswer());
		assertEquals("S1", event.getAutoAnswer());
	}
}
