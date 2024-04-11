package com.kep.portal.controller.issue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.portal.model.dto.issue.IssueSearchCondition;
import com.kep.portal.service.assign.AssignProducer;
import com.kep.portal.service.issue.IssueService;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(IssueController.class)
@Slf4j
class IssueControllerTest {

	@TestConfiguration
	public static class ContextConfig {
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapperConfig().objectMapper();
		}
	}

	@Resource
	private MockMvc mockMvc;
	@MockBean
	private IssueService issueService;
	@MockBean
	private AssignProducer assignProducer;

	private final EasyRandom easyRandom = new EasyRandom();
	private final int MAX_ELEMENT = 10;

	@BeforeEach
	void BeforeEach() throws Exception {

		List<IssueDto> issueDtos = new ArrayList<>();
		for (int i = 0; i < MAX_ELEMENT; i++) {
			IssueDto issueDto = easyRandom.nextObject(IssueDto.class);
			issueDto.setMember(null);
			issueDto.setIssueCategory(null);
			issueDto.setCustomerId(null);
			issueDtos.add(issueDto);
		}
		Page<IssueDto> page = new PageImpl<>(issueDtos);
		given(issueService.search(any(IssueSearchCondition.class), any(Pageable.class)))
				.willReturn(page);
	}

	@Disabled
	@Test
	@WithMockUser(value = "spring")
	void testGet() throws Exception {

		mockMvc.perform(get("/api/v1/issue")
						.contentType(MediaType.APPLICATION_JSON)
						.param("branch_id", "19")
						.param("status", "open"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(ApiResultCode.succeed.name()))
				.andExpect(jsonPath("$.total_element").value(MAX_ELEMENT));
	}
}
