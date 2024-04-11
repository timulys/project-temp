package com.kep.portal.config.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.portal.service.assign.AssignConsumer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import javax.annotation.Resource;

/**
 * 큐서버 설정
 */
@Configuration
public class QueueConfig { //implements RabbitListenerConfigurer {

	@Resource
	private ConnectionFactory connectionFactory;

	/**
	 * 어드민, 서버에 어드민 명령어 처리가 필요한 경우 사용
	 */
	@Bean
	public RabbitAdmin rabbitAdmin() {

		return new RabbitAdmin(connectionFactory);
	}

	/**
	 * 직렬화, 역직렬화 설정
	 */
	@Bean
	public MessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {

		return new Jackson2JsonMessageConverter(objectMapper);
	}

	/**
	 * 이벤트 처리 객체
	 */
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jackson2JsonMessageConverter) {

		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
		return rabbitTemplate;
	}

	// ////////////////////////////////////////////////////////////////////////
	// 배정 스케줄러용 큐 설정
	// ////////////////////////////////////////////////////////////////////////
	public static final String ASSIGN_QUEUE_NAME = "ASSIGN_QUEUE";
	public static final String ASSIGN_EXCHANGE_NAME = "ASSIGN_EXCHANGE";
	public static final String ASSIGN_ROUTING_KEY_NAME = "ASSIGN_ROUTING_KEY";

	@Bean
	public Queue queueToPortal() {
		return new Queue(ASSIGN_QUEUE_NAME, true, false, false);
	}
	@Bean
	public DirectExchange exchangeToPortal() {
		return new DirectExchange(ASSIGN_EXCHANGE_NAME);
	}
	@Bean
	public Binding bindingToPortal(Queue queueToPortal, DirectExchange exchangeToPortal) {
		return BindingBuilder.bind(queueToPortal).to(exchangeToPortal).with(ASSIGN_ROUTING_KEY_NAME);
	}

	/**
	 * 컨슈머 (배정 처리) 등록
	 */
	@Bean
	public SimpleMessageListenerContainer toPortalMessageListenerContainer(AssignConsumer assignConsumer) {

		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
		simpleMessageListenerContainer.setQueueNames(ASSIGN_QUEUE_NAME);
		simpleMessageListenerContainer.setConcurrentConsumers(10);
		simpleMessageListenerContainer.setMessageListener(assignConsumer);

		return simpleMessageListenerContainer;
	}

	// ////////////////////////////////////////////////////////////////////////
	// 기본값 설정
	// ////////////////////////////////////////////////////////////////////////
	@Bean
	public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry(){
		return new RabbitListenerEndpointRegistry();
	}

	@Bean
	public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
		return new DefaultMessageHandlerMethodFactory();
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {

		final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setDefaultRequeueRejected(false);
		factory.setConcurrentConsumers(3);
		factory.setMaxConcurrentConsumers(10);
		factory.setPrefetchCount(1000);
		factory.setMissingQueuesFatal(false);
		return factory;
	}
}
