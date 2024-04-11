package com.kep.platform.service;

import com.kep.core.model.dto.event.PlatformEventDto;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.platform.config.queue.QueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 플랫폼으로 이벤트 전송, 프로듀서
 */
@Component
@Slf4j
public class SendToPlatformProducer {

	@Resource
	private RabbitTemplate rabbitTemplate;

	/**
	 * 큐서버로 메세지 전송
	 *
	 * @param platformEventDto 이벤트
	 */
	@Retryable(value = {AmqpException.class}
	, maxAttempts = 4
			, backoff = @Backoff(delay = 1000, multiplier = 2))
	public void sendMessage(@NotNull @Valid PlatformEventDto platformEventDto) {

		log.info("SEND TO PLATFORM PRODUCER, TRACK KEY: {}, START", platformEventDto.getTrackKey());
		String exchangeName = getExchangeName(platformEventDto);
		String routingKey = getRoutingKey(platformEventDto);
		log.debug("SEND TO PLATFORM PRODUCER, TRACK KEY: {}, EXCHANGE: {}, ROUTING: {}",
				platformEventDto.getTrackKey(), exchangeName, routingKey);

		if (!ObjectUtils.isEmpty(exchangeName) && !ObjectUtils.isEmpty(routingKey)) {
			rabbitTemplate.convertAndSend(exchangeName, routingKey, platformEventDto, m -> {
				m.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
				return m;
			});
		} else {
			log.error("SEND TO PLATFORM PRODUCER, TRACK KEY: {}, FAILED, EXCHANGE: {}, ROUTING: {}",
					platformEventDto.getTrackKey(), exchangeName, routingKey);
		}

		log.debug("SEND TO PLATFORM PRODUCER, TRACK KEY: {}, END", platformEventDto.getTrackKey());
	}

	/**
	 * 이벤트별 Exchange Name
	 *
	 * @param platformEventDto 이벤트
	 * @return Exchange Name
	 */
	private String getExchangeName(@NotNull @Valid PlatformEventDto platformEventDto) {

		if (PlatformType.kakao_counsel_talk.equals(platformEventDto.getPlatformType())) {
			return QueueConfig.SEND_TO_PLATFORM_EXCHANGE_NAME;
		} else if (PlatformType.kakao_alert_talk.equals(platformEventDto.getPlatformType())) {
			return QueueConfig.SEND_TO_KAKAO_ALERT_TALK_EXCHANGE_NAME;
		} else if (PlatformType.kakao_friend_talk.equals(platformEventDto.getPlatformType())) {
			return QueueConfig.SEND_TO_KAKAO_FRIEND_TALK_EXCHANGE_NAME;
		} else if (PlatformType.solution_web.equals(platformEventDto.getPlatformType())) {
			return QueueConfig.SEND_TO_PLATFORM_EXCHANGE_NAME;
		} else {
			return null;
		}
	}

	/**
	 * 이벤트별 Routing Key
	 *
	 * @param platformEventDto 이벤트
	 * @return Routing Key
	 */
	private String getRoutingKey(@NotNull @Valid PlatformEventDto platformEventDto) {

		if (PlatformType.kakao_counsel_talk.equals(platformEventDto.getPlatformType())) {
			return QueueConfig.SEND_TO_PLATFORM_ROUTING_KEY_NAME;
		} else if (PlatformType.kakao_alert_talk.equals(platformEventDto.getPlatformType())) {
			return QueueConfig.SEND_TO_KAKAO_ALERT_TALK_ROUTING_KEY_NAME;
		} else if (PlatformType.kakao_friend_talk.equals(platformEventDto.getPlatformType())) {
			return QueueConfig.SEND_TO_KAKAO_FRIEND_TALK_ROUTING_KEY_NAME;
		} else if (PlatformType.solution_web.equals(platformEventDto.getPlatformType())) {
			return QueueConfig.SEND_TO_PLATFORM_ROUTING_KEY_NAME;
		} else {
			return null;
		}
	}

	/**
	 * Recover
	 *
	 * @param e AmqpException
	 * @param platformEventDto 이벤트
	 */
	@Recover
	private void recover(AmqpException e, PlatformEventDto platformEventDto) {

		log.warn("SEND TO PLATFORM PRODUCER, TRACK KEY: {}, FAILED, SAVE EVENT", platformEventDto.getTrackKey());
		// TODO: failover 정책
		throw e;
	}
}
