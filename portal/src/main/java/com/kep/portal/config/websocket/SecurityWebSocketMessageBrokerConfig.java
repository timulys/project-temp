package com.kep.portal.config.websocket;

import com.kep.portal.config.property.SocketProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import javax.annotation.Resource;

/**
 * 웹 소켓 설정
 */
@Configuration
@EnableWebSocketMessageBroker
@Profile(value = "!rabbitmq-cluster")
@Slf4j
public class SecurityWebSocketMessageBrokerConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Resource
	private SocketProperty socketProperty;

	/**
	 * 웹소켓 트래픽 접근 제한 설정
	 */
	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {

		messages
				// CONNECT 등 기타 요청 허용
				.nullDestMatcher()
				.permitAll()

				// SUBSCRIBE 요청 허용
				.simpSubscribeDestMatchers("/topic/**", "/queue/**")
				.permitAll()

				// MESSAGE 요청 거부 (Rest API 사용)
				.anyMessage()
				.denyAll()
		;
	}

	/**
	 * 메세지 브로커, STOMP 설정
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		// Map to @Controller > @MessageMapping, 미사용
		registry
				.setUserDestinationPrefix("/user")
				.setApplicationDestinationPrefixes("/app");
		// Map to Message Broker
		registry
				.enableStompBrokerRelay("/topic")
//				.enableStompBrokerRelay("/topic", "/queue")
				.setRelayHost(socketProperty.getMessageBroker().getHost())
				.setRelayPort(socketProperty.getMessageBroker().getPort())
				.setClientLogin(socketProperty.getMessageBroker().getUsername())
				.setClientPasscode(socketProperty.getMessageBroker().getPassword())
				.setSystemLogin(socketProperty.getMessageBroker().getUsername())
				.setSystemPasscode(socketProperty.getMessageBroker().getPassword())
				.setVirtualHost(socketProperty.getMessageBroker().getVirtualHost())
//				.setTaskScheduler()
				.setSystemHeartbeatSendInterval(socketProperty.getMessageBroker().getHeartbeatInterval())
				.setSystemHeartbeatReceiveInterval(socketProperty.getMessageBroker().getHeartbeatInterval());
	}

	/**
	 * STOMP, End point (클라이언트에서 접근 경로) 설정
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		registry.addEndpoint(socketProperty.getEndpoint())
				.setAllowedOriginPatterns("*")
				.withSockJS();
	}

	/**
	 * CORS 설정
	 */
	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}

	// ////////////////////////////////////////////////////////////////////////
	// 이벤트별 핸들러
	// ////////////////////////////////////////////////////////////////////////
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {

		log.debug("CONNECT EVENT: {}", event);

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		if (headerAccessor.getSessionAttributes() != null) {
			for (String key : headerAccessor.getSessionAttributes().keySet()) {
				log.debug("WS SESSION, {}: {}", key, headerAccessor.getSessionAttributes().get(key));
			}
		}
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

		log.debug("DISCONNECT EVENT: {}", event);

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		if (headerAccessor.getSessionAttributes() != null) {
			for (String key : headerAccessor.getSessionAttributes().keySet()) {
				log.debug("WS SESSION, {}: {}", key, headerAccessor.getSessionAttributes().get(key));
			}
		}
	}

	@EventListener
	public void handleSubscribeListener(SessionSubscribeEvent event) {

		log.debug("SUBSCRIBE EVENT: {}", event);

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		if (headerAccessor.getSessionAttributes() != null) {
			for (String key : headerAccessor.getSessionAttributes().keySet()) {
				log.debug("WS SESSION, {}: {}", key, headerAccessor.getSessionAttributes().get(key));
			}
		}
	}

	@EventListener
	public void handleUnsubscribeListener(SessionUnsubscribeEvent event) {

		log.debug("UNSUBSCRIBE EVENT: {}", event);

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		if (headerAccessor.getSessionAttributes() != null) {
			for (String key : headerAccessor.getSessionAttributes().keySet()) {
				log.debug("WS SESSION, {}: {}", key, headerAccessor.getSessionAttributes().get(key));
			}
		}
	}
}
