package com.kep.platform.config.security;

import com.kep.core.model.dto.ApiResultCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"local"})
@Slf4j
class SecurityConfigTest {

	@Resource
	private WebApplicationContext applicationContext;

	@Value("/actuator/health")
	private String testUrl;

	private MockMvc mvc;

	@BeforeEach
	public void beforeEach() {
		mvc = MockMvcBuilders.webAppContextSetup(applicationContext)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.alwaysDo(print())
				.build();
	}

	@Test
	@DisplayName("잘못된 URL 접근시, 404")
	@WithMockUser
	void givenNothing_whenGetInvalidResource_thenNotFound() throws Exception {

		mvc.perform(get("/wrong-path")
						.header("X-API-Key", "beerholic2")
//						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("API Key 사용시, 200")
	public void givenApiKey_whenGetEcho_thenOk() throws Exception {

		mvc.perform(get(testUrl)
						.header("X-API-Key", "beerholic4")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"));
	}

	@Test
	@DisplayName("API Key 없을 경우, 401")
//	@WithMockUser
	public void givenNoApiKey_whenGetEcho_thenUnauthorized() throws Exception {

		mvc.perform(get(testUrl)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.code").value(ApiResultCode.failed.name()));
	}

	@Test
	@DisplayName("잘못된 API Key 사용시, 401")
	public void givenInvalidApiKey_whenGetEcho_thenUnauthorized() throws Exception {

		mvc.perform(get(testUrl)
						.header("X-API-Key", "wrong_api_key")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.code").value(ApiResultCode.failed.name()));
	}
}
