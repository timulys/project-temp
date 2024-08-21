package com.kep.platform.service.kakao.counsel;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.platform.model.dto.center.Message;
import com.kep.platform.model.dto.center.SystemMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.platform.config.property.PlatformProperty;
import com.kep.platform.model.dto.center.KakaoSystemMessage;

import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class KakaoCounselCenterService {

	// 카카오 시스템 메세지
	private static final String SYSTEM_MESSAGE_PATH = "/v3/{API_KEY}/sm";

	@Resource
	private PlatformProperty platformProperty;
	@Resource(name = "externalWebClientBuilder")
	private WebClient.Builder externalWebClientBuilder;
	@Resource
	private ObjectMapper objectMapper;

	@Resource
	private RestTemplate externalRestTemplate;

	private String getApiKey() {
		return platformProperty.getPlatforms().get(PlatformType.kakao_counsel_center.name()).getApiKey();
	}

	private String getBaseUrl() {
		return platformProperty.getPlatforms().get(PlatformType.kakao_counsel_center.name()).getApiBaseUrl();
	}

	/**
	 * 무과금 메시지 조회
	 */
	public String getSystemMessage(String serviceKey, String messageType) throws Exception {

		String apiKey = getApiKey();

		WebClient client = externalWebClientBuilder.baseUrl(getBaseUrl()).build();

		SystemMessageDto systemMessage = client.get().uri(uriBuilder -> uriBuilder
				.path("/v3/" + apiKey + "/sm")
				.queryParam("senderKey", serviceKey)
				.build()
		).retrieve().bodyToMono(SystemMessageDto.class).block();
		List<Message> messages = new ArrayList<>();

		if (messageType != null) {
			if (systemMessage.getData() != null)
				messages = systemMessage.getData().get(0).getMessages().stream().filter(item ->
						item.getMessageType().matches(messageType)
				).collect(Collectors.toList());
		} else {
			messages = systemMessage.getData().get(0).getMessages();
		}

		String response = objectMapper.writeValueAsString(messages);

		return response;
	}

	/**
	 * 카카오 시스템 메세지 ISSUE PAYLOAD 변환
	 */
	public List<IssuePayload> systemMessage(String serviceKey) {
		List<IssuePayload> issuePayloads = new ArrayList<>();
		try {
			// WebClient에 시스템 proxy 설정(자동 인식하지 않음
			ReactorClientHttpConnector reactorClientHttpConnector = new ReactorClientHttpConnector(HttpClient.create().proxyWithSystemProperties());

			String url = getBaseUrl() + SYSTEM_MESSAGE_PATH.replace("{API_KEY}" , getApiKey()) + "?senderKey=75255f38d08ff785b5695b43e4d2fdc6819c77de&id=6734";
			KakaoSystemMessage systemMessage = externalRestTemplate.getForObject(url , KakaoSystemMessage.class);

			log.info("@@eddie.j getApiKey() = {} " , getApiKey());
			log.info("@@eddie.j getBaseUrl() = {} " , getBaseUrl());
			log.info("@@eddie.j serviceKey = {} " , serviceKey);
			log.info("@@eddie.j url = {} " , url);

			// /api/v3/{partner_key}/sender


			// 잠시 테스트를 위해서 '발신프로필 정보를 조회' API 테스트
			String test_url = getBaseUrl() + "/api/v3/" + serviceKey + "/sender";
			String result = externalRestTemplate.getForObject(test_url , String.class);

			log.info("@@eddie.j test_url = {}" , test_url);
			log.info("@@eddie.j result = {}" , result);



			/*
			KakaoSystemMessage systemMessage = WebClient.builder().clientConnector(reactorClientHttpConnector).baseUrl(getBaseUrl()).build().get()
					.uri(uriBuilder -> uriBuilder.path(SYSTEM_MESSAGE_PATH).queryParam("senderKey", serviceKey).build(getApiKey())).accept(MediaType.APPLICATION_JSON).httpRequest(httpRequest -> {
						HttpClientRequest reactorRequest = httpRequest.getNativeRequest();
						reactorRequest.responseTimeout(Duration.ofSeconds(60));
					}).retrieve().onStatus(HttpStatus::is4xxClientError, response -> {
						log.error("KAKAO CENTER SYSTEM MESSAGE , STATUS:{} , HEADERS:{}", response.statusCode(), response.headers());
						return null;
					}).onStatus(HttpStatus::is5xxServerError, response -> {
						log.error("KAKAO CENTER SYSTEM MESSAGE , STATUS:{} , HEADERS:{}", response.statusCode(), response.headers());
						return null;
					}).bodyToMono(KakaoSystemMessage.class).block();
			*/
			log.info("SYSTEM MESSAGE KAKAO MESSAGE:{}", systemMessage);

			Assert.notNull(systemMessage, "NOT NULL KAKAO SYSTEM MESSAGE");
			if ("200".equals(systemMessage.getCode())) {
				if (!ObjectUtils.isEmpty(systemMessage.getData())) {
					systemMessage.getData().stream().peek(item -> {
						item.getMessages().stream().peek(message -> {
							issuePayloads.add(IssuePayload.builder().version(IssuePayload.CURRENT_VERSION)
									.chapters(new IssuePayload(IssuePayload.Section.builder().type(IssuePayload.SectionType.text).data(message.getContent()).extra(message.getMessageType()).build()).getChapters())
									.build());
						}).collect(Collectors.toList());
					}).collect(Collectors.toList());

					if (!ObjectUtils.isEmpty(issuePayloads)) {
						log.info("ISSUE PAYLOAD MESSAGE:{}", issuePayloads);
						return issuePayloads;
					}
					return issuePayloads;
				}
			}
		} catch (Exception e) {
			log.error("KAKAO SYSTEM MESSAGE :{}", e.getMessage(), e);
		}

		List<String> code = Arrays.asList("S1", "S2", "S3", "S4", "ED", "ER", "BL", "SB");
		for (int i = 0; i < code.size(); i++) {
			issuePayloads.add(new IssuePayload(IssuePayload.Section.builder().type(IssuePayload.SectionType.platform_answer).data("상담톡 시스템메세지(API)에 접근할수 없습니다.").extra(code.get(i)).build()));
		}
		return issuePayloads;
	}
}
