package com.kep.portal.service.socket;

import com.kep.core.model.dto.issue.IssueLogDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * 소켓으로 내려주는 역할만
 * TODO: Destination 에 따라 분리
 */
@Service
@Transactional
public class SocketService {

	@Resource
	private RestTemplate restTemplate;
	@Resource
	private SimpMessagingTemplate simpMessagingTemplate;

	public boolean send(IssueLogDto issueLogDto) {

		// TODO: save to entity

		// TODO: send to channel
		return false;
	}
}
