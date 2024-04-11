package com.kep.portal.service.customer;

import com.kep.portal.model.dto.customer.*;
import com.kep.portal.model.entity.customer.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.validation.constraints.NotNull;

@Service
@Profile({"legacy-customer"})
@Slf4j
/*
 *
 *  @수정일자      / 수정한사람      / 수정내용
 *  2023.04.12 / asher.shin   / 카테고리별 조회 및 즐겨찾기 변경 추가
 *
 */
public class CustomerServiceLegacyImpl implements CustomerService {

	@Override
	public Customer findById(Long id) {

		log.error("NotImplementedException");
		return null;
	}

	@Override
	public List<Customer> search(String subject, String query) {

		log.error("NotImplementedException");
		return null;
	}

}
