package com.kep.legacy.controller;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.legacy.model.entity.Customer;
import com.kep.legacy.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 솔루션 고객 정보 (고객사에서 호출)
 */
@RestController
@RequestMapping("/api/v1/counsel-portal/customer")
//@RequestMapping("/api/v1/portal/customer")
@Slf4j
public class CustomerController {

	@Resource
	private CustomerService customerService;

	@GetMapping
	public ResponseEntity<ApiResult<List<Customer>>> getAll(
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(required = false) Map<String, Object> requestParams) {

		log.info("CUSTOMER, GET ALL, PARAMS: {}", requestParams);

		List<Customer> customers = customerService.findAll(requestParams);
		ApiResult<List<Customer>> response = ApiResult.<List<Customer>>builder()
				.code(ApiResultCode.succeed)
				.payload(customers)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
