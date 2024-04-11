package com.kep.portal.config.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.portal.config.property.CoreProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest
@Slf4j
class LegacyClientTest {

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private RestTemplate restTemplate;
	@Resource
	private WebClient.Builder webClientBuilder;
	@Resource
	private CoreProperty coreProperty;

	@Disabled("legacy 모듈 올려야 테스트 가능")
	@Test
	void testClient() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Api-Key", "beerholic2");
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

		int i = 1;
		for (; i <= 2; i++) {
			ResponseEntity<ApiResult<Map<String, Object>>> responsePlatform = restTemplate.exchange(
					coreProperty.getLegacyServiceUri() + "/api/v1/mock/echo", HttpMethod.GET, request, new ParameterizedTypeReference<ApiResult<Map<String, Object>>>() {
					});
			log.info("{}: STATUS: {}, BODY: {}", i, responsePlatform.getStatusCode(), objectMapper.writeValueAsString(responsePlatform.getBody()));
		}
	}

	@Disabled("legacy 모듈 올려야 테스트 가능")
	@Test
	void testLoadBalancedWebClient() {

		WebClient webClient = webClientBuilder
//				.filter(lbFunction)
				.build();

		int i = 1;
		for (; i <= 2; i++) {
			ResponseEntity<String> response = webClient.get()
					.uri(coreProperty.getLegacyServiceUri() + "/api/v1/mock/echo")
					.retrieve()
					.toEntity(String.class)
					.block();

			if (response != null) {
				log.info("{}, STATUS: {}, BODY: {}", i, response.getStatusCodeValue(), response.getBody());
			} else {
				log.error("{}, ERROR", i);
			}
		}
	}
}
