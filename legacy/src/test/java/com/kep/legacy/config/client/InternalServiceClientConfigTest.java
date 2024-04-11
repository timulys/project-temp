package com.kep.legacy.config.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.legacy.config.property.CoreProperty;
import com.kep.legacy.config.property.PortalProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class InternalServiceClientConfigTest {

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private RestTemplate restTemplate;
	@Resource
	private CoreProperty coreProperty;
	@Resource
	private PortalProperty portalProperty;

	@Disabled("portal 모듈 올려야 테스트 가능")
	@Test
	void testClient() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("suid", "3027505611");
		requestBody.put("ci", "LPahT3DrpCLPHkG4OJFQJbs11wlQCxBWEWYo5sHTFs6ul9MaqHgu9a63EDpWzVCjWnbietEFhyPwKRkyr9++xQ==");
		requestBody.put("vndr_cust_no", "1234567");
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
		ResponseEntity<ApiResult<Map<String, Object>>> response = restTemplate.exchange(
				 "https://rbsdev.bnkcapital.co.kr/api/ckchat/cust",
				HttpMethod.POST, request, new ParameterizedTypeReference<ApiResult<Map<String, Object>>>() {
				});
		log.info("{}", objectMapper.writeValueAsString(response.getBody()));
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getPayload());
	}
}
