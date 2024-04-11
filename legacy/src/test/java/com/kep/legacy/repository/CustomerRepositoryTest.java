package com.kep.legacy.repository;

import com.kep.legacy.model.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CustomerRepositoryTest {

	@Resource
	private CustomerRepository customerRepository;

	@Test
	void findAll() {

		List<Customer> customers = customerRepository.findAll(Collections.emptyMap());
		assertNotNull(customers);
	}
}