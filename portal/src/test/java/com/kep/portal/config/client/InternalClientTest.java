package com.kep.portal.config.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class InternalClientTest {

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private RestTemplate restTemplate;
	@Resource
	private WebClient.Builder webClientBuilder;
//	@Resource
//	private ReactorLoadBalancerExchangeFilterFunction lbFunction;

	@Disabled("platform 모듈 올려야 테스트 가능")
	@Test
	void testClient() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Api-Key", "beerholic2");
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);
		ResponseEntity<ApiResult<Map<String, Object>>> response = restTemplate.exchange(
				"http://platform-service/platform/api/v1/mock/echo", HttpMethod.GET,
				request, new ParameterizedTypeReference<ApiResult<Map<String, Object>>>() {});
		log.info("{}", objectMapper.writeValueAsString(response.getBody()));
		ResponseEntity<ApiResult<Map<String, Object>>> responseBody = restTemplate.exchange(
				"http://legacy-service/legacy/api/v1/mock/echo", HttpMethod.GET,
				request, new ParameterizedTypeReference<ApiResult<Map<String, Object>>>() {});
		log.info("{}", objectMapper.writeValueAsString(responseBody.getBody()));
	}

	@Disabled("legacy 모듈 올려야 테스트 가능")
	@Test
	public void testLoadBalancedRestTemplate() {

		int i = 1;
//		for (; i <= 10; i++) {
			try {
				HttpHeaders headers = new HttpHeaders();
				HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);
				ResponseEntity<String> response = restTemplate.exchange(
						"http://legacy-service/legacy/api/v1/mock/wrong", HttpMethod.GET,
						request, String.class);

				if (response.getBody() != null) {
					log.info("{}, STATUS: {}, BODY: {}", i, response.getStatusCodeValue(), response.getBody());
				} else {
					log.error("{}, ERROR", i);
				}

				TimeUnit.SECONDS.sleep(2);
			} catch (Exception e) {
				log.error("{}, MESSAGE: {}", i, e.getLocalizedMessage());
			}
//		}
	}

	@Disabled("legacy 모듈 올려야 테스트 가능")
	@Test
	public void testLoadBalancedWebClient() {

		WebClient webClient = webClientBuilder
//				.filter(lbFunction)
				.build();

		int i = 1;
//		for (; i <= 10; i++) {
			try {
				ResponseEntity<String> response = webClient.get()
						.uri("http://legacy-service/legacy/api/v1/mock/echo")
						.retrieve()
						.toEntity(String.class)
						.block();

				if (response != null) {
					log.info("{}, STATUS: {}, BODY: {}", i, response.getStatusCodeValue(), response.getBody());
				} else {
					log.error("{}, ERROR", i);
				}

				TimeUnit.SECONDS.sleep(2);
			} catch (Exception e) {
				log.error("{}, MESSAGE: {}", i, e.getLocalizedMessage());
			}
//		}
	}
}
