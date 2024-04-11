package com.kep.platform.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.event.PlatformEventDto;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.platform.AuthorizeType;
import com.kep.core.model.dto.platform.kakao.KakaoBizTalkSendResponse;
import com.kep.platform.config.property.CoreProperty;
import com.kep.platform.config.property.PortalProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Portal API 클라이언트
 */
@Service
@Slf4j
public class PortalClient {

	@Resource
	private RestTemplate restTemplate;
	@Resource
	private WebClient.Builder webClientBuilder;
	@Resource
	private CoreProperty coreProperty;
	@Resource
	private PortalProperty portalProperty;
	
	/**
	 * 오픈 이벤트 전달
	 */
	@Retryable(value = {RestClientException.class}
			, maxAttempts = 3
			, backoff = @Backoff(delay = 1000, multiplier = 2))
	public IssueDto open(@NotNull @Valid PlatformEventDto platformEventDto) {

		HttpHeaders headers = getHeaders(platformEventDto);

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(platformEventDto.getParams(), headers);
		String baseUrl = coreProperty.getPortalServiceUri() + portalProperty.getApiBasePath();
		String requestUrl = baseUrl + portalProperty.getOpenPath();

		log.info("SEND TO PORTAL, OPEN, TRACK KEY: {}, HEADER: {}, BODY: {}",
				platformEventDto.getTrackKey(), headers, platformEventDto.getParams());
		ResponseEntity<ApiResult<IssueDto>> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST,
				request, new ParameterizedTypeReference<ApiResult<IssueDto>>() {});
		log.info("SEND TO PORTAL, OPEN, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
				platformEventDto.getTrackKey(), responseEntity.getStatusCode(), responseEntity.getBody());

		ApiResult<IssueDto> response = responseEntity.getBody();
		Assert.notNull(response, "empty response");
		Assert.notNull(response.getPayload(), "empty payload");
		Assert.notNull(response.getPayload().getId(), "invalid issue id");

		return response.getPayload();
	}

	/**
	 * 메세지 이벤트 전달
	 */
	@Retryable(value = {RestClientException.class}
			, maxAttempts = 3
			, backoff = @Backoff(delay = 1000, multiplier = 2))
	public void message(
			@NotNull @Valid PlatformEventDto platformEventDto, IssuePayload issuePayload) throws Exception {

		HttpHeaders headers = getHeaders(platformEventDto);
		HttpEntity<IssuePayload> request = new HttpEntity<>(issuePayload, headers);
		String baseUrl = coreProperty.getPortalServiceUri() + portalProperty.getApiBasePath();
		String requestUrl = baseUrl + portalProperty.getMessagePath();

		log.info("SEND TO PORTAL, MESSAGE, TRACK KEY: {}, HEADER: {}, BODY: {}", platformEventDto.getTrackKey(), headers, issuePayload);
		ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, String.class);
		log.info("SEND TO PORTAL, MESSAGE, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
				platformEventDto.getTrackKey(), responseEntity.getStatusCode(), responseEntity.getBody());
	}

	/**
	 * 세션 종료 이벤트 전달
	 */
	@Retryable(value = {RestClientException.class}
			, maxAttempts = 3
			, backoff = @Backoff(delay = 1000, multiplier = 2))
	public void close(@NotNull @Valid PlatformEventDto platformEventDto) {

		HttpHeaders headers = getHeaders(platformEventDto);
		HttpEntity<String> request = new HttpEntity<>(headers);
		String baseUrl = coreProperty.getPortalServiceUri() + portalProperty.getApiBasePath();
		String requestUrl = baseUrl + portalProperty.getClosePath();

		log.info("SEND TO PORTAL, END, TRACK KEY: {}, HEADER: {}", platformEventDto.getTrackKey(), headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, String.class);
		log.info("SEND TO PORTAL, END, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
				platformEventDto.getTrackKey(), responseEntity.getStatusCode(), responseEntity.getBody());
	}

	/**
	 * 메세지 송신 이벤트 콜백
	 */
	@Retryable(value = {Exception.class}
			, maxAttempts = 3
			, backoff = @Backoff(delay = 1000, multiplier = 2))
	public void callback(@NotNull @Valid PlatformEventDto platformEventDto, Boolean sentResult) {

		if (!ObjectUtils.isEmpty(platformEventDto.getEventKey())) {
			HttpHeaders headers = getHeaders(platformEventDto);
			headers.add("X-Event-Key", platformEventDto.getEventKey());
			HttpEntity<Boolean> request = new HttpEntity<>(sentResult, headers);
			String baseUrl = coreProperty.getPortalServiceUri() + portalProperty.getApiBasePath();
			String requestUrl = baseUrl + portalProperty.getCallbackPath();

			log.info("SEND TO PORTAL, CALLBACK, TRACK KEY: {}, HEADER: {}, BODY: {}", platformEventDto.getTrackKey(), headers, sentResult);
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.PUT, request, String.class);
			log.info("SEND TO PORTAL, CALLBACK, TRACK KEY: {}, RETURN CODE: {}, RESPONSE BODY: {}",
					platformEventDto.getTrackKey(), responseEntity.getStatusCode(), responseEntity.getBody());
		}
	}

	/**
	 * 고객 인증 완료
	 */
	@Retryable(value = {Exception.class}
			, maxAttempts = 3
			, backoff = @Backoff(delay = 1000, multiplier = 2))
	public void authorized(@NotNull AuthorizeType authorizeType, @NotEmpty Map<String, Object> userInfo, @NotNull Long trackKey) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Authorize-Type", authorizeType.name());
		headers.add("X-Track-Key", trackKey.toString());
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(userInfo, headers);
		String baseUrl = coreProperty.getPortalServiceUri() + portalProperty.getApiBasePath();
		String requestUrl = baseUrl + portalProperty.getAuthorizedPath();

		log.info("SEND TO PORTAL, AUTHORIZED, TRACK KEY: {}, TYPE: {}, BODY: {}",
				trackKey, authorizeType, userInfo);
		ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, String.class);
		log.info("SEND TO PORTAL, AUTHORIZED, TRACK KEY: {}, TYPE: {}, RETURN CODE: {}, RESPONSE BODY: {}",
				trackKey, authorizeType, responseEntity.getStatusCode(), responseEntity.getBody());
		
	}

	private HttpHeaders getHeaders(@NotNull @Valid PlatformEventDto platformEventDto) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Platform-Type", platformEventDto.getPlatformType().name());
		headers.add("X-Service-Key", platformEventDto.getServiceKey());
		headers.add("X-User-Key", platformEventDto.getUserKey());
		headers.add("X-Track-Key", platformEventDto.getTrackKey().toString());

		return headers;
	}

	public void sendMessageResponse(KakaoBizTalkSendResponse response){
		WebClient webClient = webClientBuilder.baseUrl(coreProperty.getPortalServiceUri() + portalProperty.getApiBasePath()).build();

		String result = webClient.put().uri("/biztalk/callback")
				.header("X-TRACK-KEY", String.valueOf(System.currentTimeMillis()))
				.bodyValue(response).retrieve().bodyToMono(String.class).block();
		log.info("result = {}", result);
	}
}