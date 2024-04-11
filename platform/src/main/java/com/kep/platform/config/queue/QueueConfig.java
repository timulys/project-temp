package com.kep.platform.config.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.platform.service.kakao.alert.SendToKakaoAlertTalkConsumer;
import com.kep.platform.service.kakao.counsel.SendToCounselTalkConsumer;
import com.kep.platform.service.SendToPortalConsumer;
import com.kep.platform.service.kakao.friend.SendToFriendTalkConsumer;
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
	// 플랫폼 -> 솔루션 큐 설정
	// ////////////////////////////////////////////////////////////////////////
	public static final String SEND_TO_PORTAL_QUEUE_NAME = "SEND_TO_PORTAL_QUEUE";
	public static final String SEND_TO_PORTAL_EXCHANGE_NAME = "SEND_TO_PORTAL_EXCHANGE";
	public static final String SEND_TO_PORTAL_ROUTING_KEY_NAME = "SEND_TO_PORTAL_ROUTING_KEY";

	@Bean
	public Queue queueToPortal() {
		return new Queue(SEND_TO_PORTAL_QUEUE_NAME, true, false, false);
	}
	@Bean
	public DirectExchange exchangeToPortal() {
		return new DirectExchange(SEND_TO_PORTAL_EXCHANGE_NAME);
	}
	@Bean
	public Binding bindingToPortal(Queue queueToPortal, DirectExchange exchangeToPortal) {
		return BindingBuilder.bind(queueToPortal).to(exchangeToPortal).with(SEND_TO_PORTAL_ROUTING_KEY_NAME);
	}

	/**
	 * 컨슈머 등록
	 */
	@Bean
	public SimpleMessageListenerContainer toPortalMessageListenerContainer(SendToPortalConsumer sendToPortalConsumer) {

		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
		simpleMessageListenerContainer.setQueueNames(SEND_TO_PORTAL_QUEUE_NAME);
		simpleMessageListenerContainer.setConcurrentConsumers(10);
		simpleMessageListenerContainer.setMessageListener(sendToPortalConsumer);

		return simpleMessageListenerContainer;
	}

	// ////////////////////////////////////////////////////////////////////////
	// 솔루션 -> 플랫폼 큐 설정, 기본값 (채팅)
	// ////////////////////////////////////////////////////////////////////////
	public static final String SEND_TO_PLATFORM_QUEUE_NAME = "SEND_TO_PLATFORM_QUEUE";
	public static final String SEND_TO_PLATFORM_EXCHANGE_NAME = "SEND_TO_PLATFORM_EXCHANGE";
	public static final String SEND_TO_PLATFORM_ROUTING_KEY_NAME = "SEND_TO_PLATFORM_ROUTING_KEY";

	@Bean
	public Queue queueToPlatform() {
		return new Queue(SEND_TO_PLATFORM_QUEUE_NAME, true, false, false);
	}
	@Bean
	public DirectExchange exchangeToPlatform() {
		return new DirectExchange(SEND_TO_PLATFORM_EXCHANGE_NAME);
	}
	@Bean
	public Binding bindingToPlatform(Queue queueToPlatform, DirectExchange exchangeToPlatform) {
		return BindingBuilder.bind(queueToPlatform).to(exchangeToPlatform).with(SEND_TO_PLATFORM_ROUTING_KEY_NAME);
	}

	/**
	 * 컨슈머 등록
	 */
	@Bean
	public SimpleMessageListenerContainer toPlatformMessageListenerContainer(SendToCounselTalkConsumer sendToCounselTalkConsumer) {

		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
		simpleMessageListenerContainer.setQueueNames(SEND_TO_PLATFORM_QUEUE_NAME);
		simpleMessageListenerContainer.setConcurrentConsumers(10);
		simpleMessageListenerContainer.setMessageListener(sendToCounselTalkConsumer);

		return simpleMessageListenerContainer;
	}

	// ////////////////////////////////////////////////////////////////////////
	// 솔루션 -> 플랫폼 큐 설정, 카카오 알림톡
	// ////////////////////////////////////////////////////////////////////////
	public static final String SEND_TO_KAKAO_ALERT_TALK_QUEUE_NAME = "SEND_TO_KAKAO_ALERT_TALK_QUEUE";
	public static final String SEND_TO_KAKAO_ALERT_TALK_EXCHANGE_NAME = "SEND_TO_KAKAO_ALERT_TALK_EXCHANGE";
	public static final String SEND_TO_KAKAO_ALERT_TALK_ROUTING_KEY_NAME = "SEND_TO_KAKAO_ALERT_TALK_ROUTING_KEY";

	@Bean
	public Queue queueToKakaoAlertTalk() {
		return new Queue(SEND_TO_KAKAO_ALERT_TALK_QUEUE_NAME, true, false, false);
	}
	@Bean
	public DirectExchange exchangeToKakaoAlertTalk() {
		return new DirectExchange(SEND_TO_KAKAO_ALERT_TALK_EXCHANGE_NAME);
	}
	@Bean
	public Binding bindingToKakaoAlertTalk(Queue queueToKakaoAlertTalk, DirectExchange exchangeToKakaoAlertTalk) {
		return BindingBuilder.bind(queueToKakaoAlertTalk).to(exchangeToKakaoAlertTalk).with(SEND_TO_KAKAO_ALERT_TALK_ROUTING_KEY_NAME);
	}

	/**
	 * 컨슈머 등록
	 */
	@Bean
	public SimpleMessageListenerContainer toKakaoAlertTalkMessageListenerContainer(SendToKakaoAlertTalkConsumer sendToKakaoAlertTalkConsumer) {

		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
		simpleMessageListenerContainer.setQueueNames(SEND_TO_KAKAO_ALERT_TALK_QUEUE_NAME);
		simpleMessageListenerContainer.setConcurrentConsumers(10);
		simpleMessageListenerContainer.setMessageListener(sendToKakaoAlertTalkConsumer);

		return simpleMessageListenerContainer;
	}

	// ////////////////////////////////////////////////////////////////////////
	// 솔루션 -> 플랫폼 큐 설정, 카카오 친구톡
	// ////////////////////////////////////////////////////////////////////////
	public static final String SEND_TO_KAKAO_FRIEND_TALK_QUEUE_NAME = "SEND_TO_KAKAO_FRIEND_TALK_QUEUE";
	public static final String SEND_TO_KAKAO_FRIEND_TALK_EXCHANGE_NAME = "SEND_TO_KAKAO_FRIEND_TALK_EXCHANGE";
	public static final String SEND_TO_KAKAO_FRIEND_TALK_ROUTING_KEY_NAME = "SEND_TO_KAKAO_FRIEND_TALK_ROUTING_KEY";

	@Bean
	public Queue queueToKakaoFriendTalk() {
		return new Queue(SEND_TO_KAKAO_FRIEND_TALK_QUEUE_NAME, true, false, false);
	}
	@Bean
	public DirectExchange exchangeToKakaoFriendTalk() {
		return new DirectExchange(SEND_TO_KAKAO_FRIEND_TALK_EXCHANGE_NAME);
	}
	@Bean
	public Binding bindingToKakaoFriendTalk(Queue queueToKakaoFriendTalk, DirectExchange exchangeToKakaoFriendTalk) {
		return BindingBuilder.bind(queueToKakaoFriendTalk).to(exchangeToKakaoFriendTalk).with(SEND_TO_KAKAO_FRIEND_TALK_ROUTING_KEY_NAME);
	}

	/**
	 * 컨슈머 등록
	 */
	@Bean
	public SimpleMessageListenerContainer toKakaoFriendAlertTalkMessageListenerContainer(SendToFriendTalkConsumer sendToFriendTalkConsumer) {

		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
		simpleMessageListenerContainer.setQueueNames(SEND_TO_KAKAO_FRIEND_TALK_QUEUE_NAME);
		simpleMessageListenerContainer.setConcurrentConsumers(10);
		simpleMessageListenerContainer.setMessageListener(sendToFriendTalkConsumer);

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

//	@Override
//	public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
//
//		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//		factory.setPrefetchCount(1);
//		factory.setConsecutiveActiveTrigger(1);
//		factory.setConsecutiveIdleTrigger(1);
//		factory.setConnectionFactory(connectionFactory);
//		rabbitListenerEndpointRegistrar.setContainerFactory(factory);
//		rabbitListenerEndpointRegistrar.setEndpointRegistry(rabbitListenerEndpointRegistry());
//		rabbitListenerEndpointRegistrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
//	}
}
