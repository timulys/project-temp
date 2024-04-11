package com.kep.portal.config.client;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ExternalClientTest {

	@Resource
	private RestTemplate externalRestTemplate;
	@Resource
	private WebClient.Builder externalWebClientBuilder;

	@Test
	void testRestTemplate() {

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);
		ResponseEntity<String> response = externalRestTemplate.exchange(
				"https://eed4fdd3-3163-4b02-8422-76a0e98c65c3.mock.pstmn.io/codeclick", HttpMethod.GET, request, String.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().contains("codeclick"));

		log.info("RESPONSE: {}", response.getBody());
	}

	@Test
	void testWebClient() {

		WebClient webClient = externalWebClientBuilder.build();

		ResponseEntity<String> response = webClient.get()
				.uri("https://eed4fdd3-3163-4b02-8422-76a0e98c65c3.mock.pstmn.io/codeclick")
				.retrieve()
				.toEntity(String.class)
				.block();

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().contains("codeclick"));

		log.info("RESPONSE: {}", response.getBody());
	}
}