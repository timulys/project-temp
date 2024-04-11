package com.kep.platform.config.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ExternalServiceClientConfigTest {

	@Resource
	private WebClient.Builder externalWebClientBuilder;
	@Resource
	private RestTemplate externalRestTemplate;
	@Resource
	private ObjectMapper objectMapper;

	@Disabled("httpbin.org 서비스 불안정")
	@Test
	void testIpByWebClient() throws Exception {

		// https://httpbin.org/ip
		// https://api.ipify.org?format=json
		String responseBody = externalWebClientBuilder.baseUrl("https://httpbin.org").build()
				.get().uri("/ip").retrieve().bodyToMono(String.class).block();
		Map<String, Object> response = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
		log.info(responseBody);
		assertNotNull(response);
//		assertNotNull(response.get("ip"));
		assertNotNull(response.get("origin"));
	}

	@Disabled("httpbin.org 서비스 불안정")
	@Test
	void testIpByRestTemplate() throws Exception {

		// https://httpbin.org/ip
		// https://api.ipify.org?format=json
		String responseBody = externalRestTemplate.getForObject("https://httpbin.org/ip", String.class);
		Map<String, Object>  response = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
		log.info(responseBody);
		assertNotNull(response);
//		assertNotNull(response.get("ip"));
		assertNotNull(response.get("origin"));
	}
}