package com.kep.portal.controller.socket;

import com.kep.core.model.dto.issue.IssueLogDto;
import com.kep.portal.config.property.SocketProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.time.ZonedDateTime;

/**
 * TODO: DELETEME
 * 이벤트 웹소켓 메세지
 */
@Deprecated
@Controller
@Slf4j
public class EventSocketController {

	@Resource
	private SocketProperty socketProperty;
	@Resource
	private SimpMessagingTemplate simpMessagingTemplate;

	@SubscribeMapping("/topic/notice")
	public void subscribeChatRoomMessage(String message) {

		log.info("MESSAGE: {}", message);
	}

	/**
	 * routing (default): /app/notice -> /topic/notice
	 */
	@MessageMapping("/notice")
	public IssueLogDto notice(IssueLogDto issueLogDto) throws Exception {

		log.info("issueLogDto: {}", issueLogDto);
		issueLogDto.setCreated(ZonedDateTime.now());

		return issueLogDto;
	}
}
