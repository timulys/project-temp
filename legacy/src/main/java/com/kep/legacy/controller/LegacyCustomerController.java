package com.kep.legacy.controller;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.legacy.LegacyCustomerDto;
import com.kep.legacy.service.LegacyCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 기간계 고객 정보 (솔루션에서 호출)
 */
@RestController
@RequestMapping("/api/v1/legacy/customer")
@Slf4j
public class LegacyCustomerController {

	@Resource
	private LegacyCustomerService legacyCustomerService;

	@GetMapping
	public ResponseEntity<ApiResult<List<LegacyCustomerDto>>> getAll(
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(required = false) Map<String, Object> requestParams) {

		log.info("LEGACY CUSTOMER, GET ALL, PARAMS: {}", requestParams);

		List<LegacyCustomerDto> customers = legacyCustomerService.getAll(requestParams);

		ApiResult<List<LegacyCustomerDto>> response = ApiResult.<List<LegacyCustomerDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(customers)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/{key}")
	public ResponseEntity<ApiResult<LegacyCustomerDto>> get(
			@RequestHeader HttpHeaders httpHeaders,
			@PathVariable(value = "key") String key,
			@RequestParam(required = false) Map<String, Object> requestParams) {

		log.info("LEGACY CUSTOMER, GET, KEY: {}, PARAMS: {}", key, requestParams);

		LegacyCustomerDto customer = legacyCustomerService.getOneMock(key, requestParams);
		if (customer == null) {
			ApiResult<LegacyCustomerDto> response = ApiResult.<LegacyCustomerDto>builder()
					.code(ApiResultCode.failed)
					.build();
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		ApiResult<LegacyCustomerDto> response = ApiResult.<LegacyCustomerDto>builder()
				.code(ApiResultCode.succeed)
				.payload(customer)
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
