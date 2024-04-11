package com.kep.portal.controller.socket;

import com.kep.core.model.dto.issue.IssueLogDto;
import com.kep.portal.controller.issue.EventByOperatorController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.security.Principal;
import java.time.ZonedDateTime;

/**
 * TODO: DELETEME
 * 웹소켓 메세지
 *
 * {@link EventByOperatorController} (API) 사용, 소켓은 내려주는 용도로만 사용
 */
@Deprecated
@Controller
@Slf4j
public class ChatSocketController {

	/**
	 * 일반 메세지
	 *
	 * routing: /app/issue/{id}/message -> /topic/issue.{id}.message
	 */
	@MessageMapping("/issue/{id}/message")
	@SendTo("/topic/issue.{id}.message")
	public IssueLogDto message(@DestinationVariable("id") String id, IssueLogDto issueLogDto, Principal user) throws Exception {

		log.info("MESSAGE, ID: {}, USER: {}, MESSAGE: {}", id, user, issueLogDto);

		// 메세지 저장
		// 채팅방 상태 업데이트
		// 소켓

		issueLogDto.setCreated(ZonedDateTime.now());
		return issueLogDto;
	}

	/**
	 * 이벤트
	 *
	 * routing: /app/issue/{id}/event -> /topic/issue.{id}.event
	 */
	@MessageMapping("/issue/{id}/event")
	@SendTo("/topic/issue.{id}.event")
	public IssueLogDto event(@DestinationVariable("id") String id, IssueLogDto issueLogDto) throws Exception {

		log.info("id: {}, event: {}", id, issueLogDto);

		return issueLogDto;
//		simpMessagingTemplate.convertAndSend("/topic/issue.chat." + id + ".event")
	}

	/**
	 * DM
	 *
	 * send from client: /user/{username}/queue/direct
	 * sub by client: /user/queue/direct
	 */
	@MessageMapping("/dm")
	@SendToUser("/queue/direct")
	public IssueLogDto whisper(IssueLogDto issueLogDto) throws Exception {

		log.info("issueLogDto: {}", issueLogDto);
		return issueLogDto;
	}
}
