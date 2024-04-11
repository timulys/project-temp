package com.kep.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.platform.service.kakao.counsel.KakaoCounselParser;
import com.rabbitmq.client.Channel;
import com.kep.core.model.dto.event.PlatformEventDto;
import com.kep.core.model.dto.event.PlatformEventType;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.platform.client.PortalClient;
import com.kep.platform.model.dto.KakaoCounselReceiveEvent;
import com.kep.platform.model.entity.PlatformSession;
import com.kep.platform.model.entity.PlatformSessionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SendToPortalConsumer implements ChannelAwareMessageListener {

	private static final int MAX_WAIT_OPEN_EVENT = 2;
	private static final int WAIT_OPEN_EVENT_SECONDS = 1;
	private static final int MAX_CONSUME = 2;

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private KakaoCounselParser kakaoCounselParser;
	@Resource
	private PortalClient portalClient;
	@Resource
	private PlatformSessionService platformSessionService;
	@Resource
	private PlatformSessionMapper platformSessionMapper;
	@Resource
	private SendToPortalProducer producer;

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {

		String body = new String(message.getBody(), StandardCharsets.UTF_8);
		log.info("SEND TO PORTAL CONSUMER, BODY: {}", body);

		PlatformEventDto platformEventDto = null;
		try {
			platformEventDto = objectMapper.readValue(body, PlatformEventDto.class);
		} catch (Exception e) {
			log.error("SEND TO PORTAL CONSUMER, NO EVENT, FAILED: {}",
					e.getLocalizedMessage(), e);
			return;
		}
		Assert.notNull(platformEventDto, "no event");

		StopWatch stopWatch = new StopWatch("SEND TO PORTAL CONSUMER - " + platformEventDto.getTrackKey());
		stopWatch.start();

		try {
			// 이벤트 타입 별로 처리
			switch (platformEventDto.getPlatformEventType()) {
				case OPEN:
					KakaoCounselReceiveEvent openEvent = objectMapper.readValue(platformEventDto.getPayload(), KakaoCounselReceiveEvent.class);
					platformEventDto.setParams(kakaoCounselParser.parseReference(openEvent));
					handleOpenEvent(platformEventDto);
					break;

				case MESSAGE:
					KakaoCounselReceiveEvent messageEvent = objectMapper.readValue(platformEventDto.getPayload(), KakaoCounselReceiveEvent.class);
					IssuePayload issuePayload = kakaoCounselParser.parseMessage(messageEvent);
					handleMessageEvent(platformEventDto, issuePayload);
					break;

				case CLOSE:
					handleCloseEvent(platformEventDto);
					break;

				default:
					log.error("SEND TO PORTAL CONSUMER, TRACK KEY: {}, UNKNOWN EVENT TYPE: {}, SKIP EVENT",
							platformEventDto.getTrackKey(), platformEventDto.getPlatformEventType());
					break;
			}
		} catch (Exception e) {
			log.error("SEND TO PORTAL CONSUMER, NO EVENT, FAILED: {}",
					e.getLocalizedMessage(), e);
		}

		stopWatch.stop();
		log.info("SEND TO PORTAL CONSUMER, END, {}ms", stopWatch.getTotalTimeMillis());
	}

	/**
	 * 오픈 이벤트 처리
	 */
	private void handleOpenEvent(
			@NotNull @Valid PlatformEventDto platformEventDto) throws Exception {

		try {
			IssueDto issueDto = portalClient.open(platformEventDto);
			// 세션 저장
			PlatformSession platformSession = platformSessionMapper.map(platformEventDto, issueDto);
			platformSessionService.save(platformSession);
		} catch (Exception e) {
			log.error("SEND TO PORTAL CONSUMER, OPEN, TRACK KEY: {}, FAILED, BODY: {}",
					platformEventDto.getTrackKey());
			throw e;
		}
	}

	/**
	 * 메세지 이벤트 처리
	 */
	private void handleMessageEvent(
			@NotNull @Valid PlatformEventDto platformEventDto,
			@NotNull @Valid IssuePayload issuePayload) throws Exception {

		String platformSessionId = PlatformSession.buildId(platformEventDto.getPlatformType()
				, platformEventDto.getServiceKey(), platformEventDto.getUserKey());
		try {
			// 세션 조회
			// 세션이 없는 경우 OPEN 이벤트를 기다림
			if (platformSessionService.findById(platformSessionId) == null) {
				log.info("SEND TO PORTAL CONSUMER, MESSAGE, SESSION NOT FOUND, WAIT");
				// OPEN 이벤트를 기다림
				TimeUnit.SECONDS.sleep(WAIT_OPEN_EVENT_SECONDS);
			}

			// OPEN 이벤트를 기다린 후에도 세션이 없는 경우, 솔루션에서 OPEN 이벤트를 생성
			if (platformSessionService.findById(platformSessionId) == null) {
				log.info("SEND TO PORTAL CONSUMER, MESSAGE, SESSION NOT FOUND, CREATE OPEN EVENT");
				Map<String, Object> params = new HashMap<>();
				params.put("mocked", true); // 솔루션에서 생성한 오픈 이벤트
				PlatformEventDto openEvent = PlatformEventDto.builder()
						.platformType(platformEventDto.getPlatformType())
						.platformEventType(PlatformEventType.OPEN)
						.serviceKey(platformEventDto.getServiceKey())
						.userKey(platformEventDto.getUserKey())
						.payload(platformEventDto.getPayload())
						.params(params)
						.created(ZonedDateTime.now())
						.build();
				handleOpenEvent(openEvent);
			}

			// 메세지 처리
			portalClient.message(platformEventDto, issuePayload);
		} catch (Exception e) {
			log.error("SEND TO PORTAL CONSUMER, MESSAGE, TRACK KEY: {}, FAILED, BODY: {}",
					platformEventDto.getTrackKey(), issuePayload);
			throw e;
		}
	}

	/**
	 * 종료 이벤트 처리
	 */
	private void handleCloseEvent(
			@NotNull @Valid PlatformEventDto platformEventDto) throws Exception {

		String platformSessionId = PlatformSession.buildId(platformEventDto.getPlatformType()
				, platformEventDto.getServiceKey(), platformEventDto.getUserKey());

		try {
			portalClient.close(platformEventDto);
			// TODO: 세션 삭제
		} catch (Exception e) {
			log.error("SEND TO PORTAL CONSUMER, CLOSE, TRACK KEY: {}, FAILED",
					platformEventDto.getTrackKey());
			throw e;
		}
	}
}
