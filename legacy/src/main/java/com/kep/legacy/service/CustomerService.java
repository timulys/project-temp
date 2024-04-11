package com.kep.legacy.service;

import com.kep.legacy.repository.CustomerRepository;
import com.kep.legacy.model.entity.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CustomerService {

	@Resource
	private CustomerRepository customerRepository;

	@Nullable
	public Customer findById(@NotNull Long id) {

		return customerRepository.findById(id);
	}

	@Nullable
	public Customer findOne(@NotNull Map<String, Object> sqlParams) {

		return customerRepository.findOne(sqlParams);
	}

	public List<Customer> findAll(@NotNull Map<String, Object> sqlParams) {

		return customerRepository.findAll(sqlParams);
	}
}
