package com.kep.portal.service.assign;

import com.kep.portal.config.queue.QueueConfig;
import com.kep.portal.model.entity.issue.IssueAssign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

/**
 * 배정 큐, 프로듀서
 */
@Component
@Slf4j
public class AssignProducer {

	@Resource
	private RabbitTemplate rabbitTemplate;

	/**
	 * 큐서버로 {@link IssueAssign} 전송
	 */
	@Retryable(value = {AmqpException.class}
			, maxAttempts = 4
			, backoff = @Backoff(delay = 1000, multiplier = 2))
	@Async("assignProducerExecutor")
	public void sendMessage(@NotNull IssueAssign issueAssign) {

		log.info("ASSIGN PRODUCER, START, ISSUE: {}", issueAssign);

		try {
			TimeUnit.MILLISECONDS.sleep(1000L);
		} catch (InterruptedException e) {
			log.error(e.getLocalizedMessage());
		}

		if (issueAssign.getId() != null) {
			rabbitTemplate.convertAndSend(QueueConfig.ASSIGN_EXCHANGE_NAME, QueueConfig.ASSIGN_ROUTING_KEY_NAME, issueAssign, m -> {
				m.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
				return m;
			});
		} else {
			log.warn("ASSIGN PRODUCER, FAILED, NOT FOUND ISSUE: {}", issueAssign);
		}
	}
}
