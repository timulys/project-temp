package com.kep.platform.service.kakao.counsel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.kep.core.model.dto.event.PlatformEventDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.platform.client.PortalClient;
import com.kep.platform.model.dto.KakaoCounselSendEvent;
import com.kep.platform.model.dto.KakaoCounselSendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 카카오 상담톡 이벤트 전송 컨슈머
 */
@Component
@Slf4j
public class SendToCounselTalkConsumer implements ChannelAwareMessageListener {

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private KakaoCounselBuilder kakaoCounselBuilder;
	@Resource
	private KakaoCounselService kakaoCounselService;
	@Resource
	private PortalClient portalClient;

	/**
	 * 큐서버에서 받은 이벤트 처리
	 */
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {

		String body = new String(message.getBody(), StandardCharsets.UTF_8);
		log.info("SEND TO PLATFORM CONSUMER, BODY: {}", body);

		PlatformEventDto platformEventDto = null;
		try {
			platformEventDto = objectMapper.readValue(body, PlatformEventDto.class);
		} catch (Exception e) {
			log.error("SEND TO PLATFORM CONSUMER, NO EVENT, FAILED: {}",
					e.getLocalizedMessage(), e);
			return;
		}
		Assert.notNull(platformEventDto, "NO PLATFORM EVENT");

		StopWatch stopWatch = new StopWatch("SEND TO PLATFORM CONSUMER - " + platformEventDto.getTrackKey());
		stopWatch.start();

		try {
			// 이벤트 타입 별로 처리
			switch (platformEventDto.getPlatformEventType()) {
				case MESSAGE:
					handleMessageEvent(platformEventDto, true);
					break;

				case CLOSE:
					handleCloseEvent(platformEventDto);
					break;

				default:
					log.error("SEND TO PLATFORM CONSUMER, TRACK KEY: {}, UNKNOWN EVENT TYPE: {}, SKIP EVENT",
							platformEventDto.getTrackKey(), platformEventDto.getPlatformEventType());
					break;
			}
		} catch (Exception e) {
			log.error("SEND TO PLATFORM CONSUMER, TRACK KEY: {}, FAILED: {}",
					platformEventDto.getTrackKey(), e.getLocalizedMessage(), e);
		}

		stopWatch.stop();
		log.info("SEND TO PLATFORM CONSUMER, END, {}ms", stopWatch.getTotalTimeMillis());
	}

	/**
	 * 메세지 이벤트 처리
	 */
	private void handleMessageEvent(
			@NotNull @Valid PlatformEventDto platformEventDto, boolean callback) throws Exception {

		List<IssuePayload> issuePayloads = objectMapper.readValue(platformEventDto.getPayload(), new TypeReference<List<IssuePayload>>() {});
		for (IssuePayload issuePayload : issuePayloads) {
			List<KakaoCounselSendEvent> kakaoCounselSendEvents = kakaoCounselBuilder.build(
					issuePayload, platformEventDto.getServiceKey(), platformEventDto.getUserKey(), platformEventDto.getEventKey());
			log.debug("{}", objectMapper.writeValueAsString(kakaoCounselSendEvents));

			for (KakaoCounselSendEvent event : kakaoCounselSendEvents) {
				try {
					KakaoCounselSendResponse response = kakaoCounselService.write(event, platformEventDto.getTrackKey());
//					if (callback) {
//						portalClient.callback(platformEventDto, response.getCode() != 500);
//					}
				} catch (Exception e) {
					log.error("SEND TO PLATFORM CONSUMER, MESSAGE, TRACK KEY: {}, FAILED, BODY: {}",
							platformEventDto.getTrackKey(), objectMapper.writeValueAsString(kakaoCounselSendEvents));
				}
			}
		}
	}

	/**
	 * 종료 이벤트 처리
	 */
	private void handleCloseEvent(
			@NotNull @Valid PlatformEventDto platformEventDto) throws Exception {

		// 메세지 이벤트가 있으면 처리
		if (!ObjectUtils.isEmpty(platformEventDto.getPayload())) {
			boolean callback = true;
			// 이벤트 키가 없는 경우 생성
			if (ObjectUtils.isEmpty(platformEventDto.getEventKey())) {
				platformEventDto.setEventKey(kakaoCounselBuilder.generateEventKey());
				callback = false;
			}
			handleMessageEvent(platformEventDto, callback);
		}

		// 종료 이벤트
		KakaoCounselSendEvent event = KakaoCounselSendEvent.builder()
				.senderKey(platformEventDto.getServiceKey())
				.userKey(platformEventDto.getUserKey())
				.build();

		try {
			kakaoCounselService.end(event, platformEventDto.getTrackKey());
		} catch (Exception e) {
			log.error("SEND TO PLATFORM CONSUMER, CLOSE, TRACK KEY: {}, FAILED, BODY: {}",
					platformEventDto.getTrackKey(), objectMapper.writeValueAsString(event));
			throw e;
		}
	}
}
