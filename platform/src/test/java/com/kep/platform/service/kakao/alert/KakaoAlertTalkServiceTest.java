package com.kep.platform.service.kakao.alert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.event.PlatformEventDto;
import com.kep.core.model.dto.event.PlatformEventType;
import com.kep.core.model.dto.platform.BizTalkMessageType;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.KakaoAlertSendEvent;
import com.kep.core.model.dto.platform.kakao.KakaoBizSearchResponse;
import com.kep.core.model.dto.platform.kakao.KakaoBizSearchSendEvent;
import com.kep.core.model.dto.platform.kakao.KakaoBizTalkSendResponse;
import com.kep.platform.service.SendToPlatformProducer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class KakaoAlertTalkServiceTest {

	@Resource
	private KakaoAlertTalkService kakaoAlertTalkService;
	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private SendToPlatformProducer producer;

	@Test
	void send() {

		List<KakaoAlertSendEvent.Message> messages = new ArrayList<>();
		messages.add(KakaoAlertSendEvent.Message.builder()
						.message("TEST MESSAGE")
						.templateCode("T_CODE_01")
						.phoneNumber("01012345678")
						.senderNo("0212345678")
				.build());

		KakaoAlertSendEvent event = KakaoAlertSendEvent.builder()
				.messageType(BizTalkMessageType.AI)
				.senderKey("1234")
				.sendMessages(messages)
				.build();

		KakaoBizTalkSendResponse response = kakaoAlertTalkService.send(event, System.currentTimeMillis());
		log.info("RESPONSE: {}", response);
	}

	@Test
	void search(){
		KakaoBizSearchSendEvent searchSendEvent = KakaoBizSearchSendEvent.builder()
				.size(1000)
				.page(1)
				.build();

		KakaoBizSearchResponse search = kakaoAlertTalkService.search(System.currentTimeMillis(), searchSendEvent);
		log.info("search = {}", search);
	}

	@Test
	void queueTest(){
		List<KakaoAlertSendEvent.Message> messages = new ArrayList<>();
		messages.add(KakaoAlertSendEvent.Message.builder()
				.message("TEST MESSAGE")
				.templateCode("T_CODE_01")
				.phoneNumber("01012345678")
				.senderNo("0212345678")
				.build());

		KakaoAlertSendEvent event = KakaoAlertSendEvent.builder()
				.messageType(BizTalkMessageType.AI)
				.senderKey("1234")
				.sendMessages(messages)
				.build();

		try {
			PlatformEventDto platformEventDto = PlatformEventDto.builder()
					.platformEventType(PlatformEventType.MESSAGE)
					.platformType(PlatformType.kakao_alert_talk)
					.created(ZonedDateTime.now())
					.payload(objectMapper.writeValueAsString(event))
					.build();

			producer.sendMessage(platformEventDto);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

	}
}