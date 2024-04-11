package com.kep.legacy.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.legacy.LegacyCustomerDto;
import com.kep.legacy.client.LegacyClient;
import com.kep.legacy.model.entity.LegacyCustomer;
import com.kep.legacy.model.entity.LegacyCustomerMapper;
import com.kep.legacy.repository.LegacyCustomerRepository;
import org.jeasy.random.EasyRandom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 기간계 고객 정보
 */
@Service
@Transactional
public class LegacyCustomerService {

	@Resource
	private LegacyCustomerRepository legacyCustomerRepository;
	@Resource
	private LegacyCustomerMapper legacyCustomerMapper;
	@Resource
	private LegacyClient legacyClient;
	@Resource
	private ObjectMapper objectMapper;

	@Nullable
	public LegacyCustomerDto getById(@NotNull Long id) {

		LegacyCustomer customer = legacyCustomerRepository.findById(id);
		return legacyCustomerMapper.map(customer);
	}

	@Nullable
	public LegacyCustomerDto getOne(@NotEmpty String key, @NotNull Map<String, Object> sqlParams) {

		sqlParams.put("ci", key); // TODO: 식별자가 CI 값일 경우
		LegacyCustomer customer = legacyCustomerRepository.findOne(sqlParams);
		return legacyCustomerMapper.map(customer);
	}

	@Nullable
	public LegacyCustomerDto getOneMock(@NotEmpty String key, @NotNull Map<String, Object> sqlParams) {
		return null;
//		long id = Math.abs(new Random().nextInt());
//		if (id % 2 == 1) {
//			return null;
//		}
//
//		return LegacyCustomerDto.builder()
//				.id(id)
//				.name("고객" + id)
//				.identifier(key)
//				.age("10 ~ 20")
//				.birthday("1980-12-13")
//				.phone("010-1111-1111")
//				.email("dfddasf@naver.com")
//				.build();
	}

	public List<LegacyCustomerDto> getAll(@NotNull Map<String, Object> sqlParams) {

		List<LegacyCustomer> customers = legacyCustomerRepository.findAll(sqlParams);
		return legacyCustomerMapper.map(customers);
	}

	public List<LegacyCustomerDto> getAll(@NotNull Map<String, Object> sqlParams,
										   @Positive Long trackKey) throws Exception {

		final String path = "/customer";
//		final String ifId = "IF_CSTM";

		Map<String, Object> requestBody = new HashMap<>();
//		requestBody.put("id", sqlParams.get("id"));
//		requestBody.put("IF_ID", ifId);

		Map<String, Object> response = legacyClient.request(path, requestBody, trackKey);
		Assert.notNull(response.get("payload"), "PAYLOAD IS NULL");
		String payload = objectMapper.writeValueAsString(response.get("payload"));
		List<LegacyCustomerDto> customers = objectMapper.readValue(payload, new TypeReference<List<LegacyCustomerDto>>() {});

		return customers;
	}
}
