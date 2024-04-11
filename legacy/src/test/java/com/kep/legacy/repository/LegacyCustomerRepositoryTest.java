package com.kep.legacy.repository;

import com.kep.legacy.model.entity.LegacyCustomer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class LegacyCustomerRepositoryTest {

	@Resource
	private LegacyCustomerRepository legacyCustomerRepository;

	@Test
	void findAll() {

		List<LegacyCustomer> customers = legacyCustomerRepository.findAll(Collections.emptyMap());
		assertNotNull(customers);
	}
}