package com.kep.portal.config.websocket;

import com.kep.portal.config.property.SocketProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompReactorNettyCodec;
import org.springframework.messaging.tcp.reactor.ReactorNettyTcpClient;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.util.Assert;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import reactor.netty.tcp.TcpClient;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 웹 소켓 설정
 */
@Configuration
@EnableWebSocketMessageBroker
@Profile(value = "rabbitmq-cluster")
@Slf4j
public class SecurityWebSocketMessageBrokerClusterConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

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
				.setTcpClient(createTcpClient())
//				.enableStompBrokerRelay("/topic", "/queue")
//				.setRelayHost(socketProperty.getMessageBroker().getHost())
//				.setRelayPort(socketProperty.getMessageBroker().getPort())
				.setClientLogin(socketProperty.getMessageBroker().getUsername())
				.setClientPasscode(socketProperty.getMessageBroker().getPassword())
				.setSystemLogin(socketProperty.getMessageBroker().getUsername())
				.setSystemPasscode(socketProperty.getMessageBroker().getPassword())
				.setVirtualHost(socketProperty.getMessageBroker().getVirtualHost())
//				.setTaskScheduler()
				.setSystemHeartbeatSendInterval(socketProperty.getMessageBroker().getHeartbeatInterval())
				.setSystemHeartbeatReceiveInterval(socketProperty.getMessageBroker().getHeartbeatInterval());
	}

	private ReactorNettyTcpClient<byte[]> createTcpClient() {

		List<String> addresses = socketProperty.getMessageBroker().getAddresses();
		Assert.isTrue(addresses != null && !addresses.isEmpty(), "property 'socket.addresses' is empty");
		Queue<String> queue = new LinkedList<>();
		Collections.addAll(queue, addresses.toArray(new String[0]));

		return new ReactorNettyTcpClient<>(
				(TcpClient tcpClient) -> {
					return tcpClient.remoteAddress(() -> {
						String address = queue.poll();
						Assert.isTrue(address != null, "address must not be null");

						String[] components = address.split(":");
						String host = components[0];
						int port = Integer.parseInt(components[1]);

						SocketAddress socketAddress = new InetSocketAddress(host, port);
						queue.add(address);

						return socketAddress;
					});
				},
				new StompReactorNettyCodec()
		);
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
