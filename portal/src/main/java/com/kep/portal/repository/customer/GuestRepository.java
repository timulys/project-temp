package com.kep.portal.repository.customer;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

	List<Guest> findAllByNameContaining(@NotEmpty String name);

	// FIXME : BNK 비즈니스 로직 관련 기능 모두 제거
	//BNK 고객번호로 검색 추가 
//	List<Guest> findAllByCustNoContaining(@NotEmpty String custNo);
	
	//BNK 싱크없는 고객 조회
//	List<Guest> findByCustomerIdIsNullAndId(Long id);

	List<Guest> findAllByCustomerIn(@NotEmpty Collection<Customer> customers);

	Guest findByCustomer(@NotEmpty Customer customer);

	List<Guest> findAllByIdIn(List<Long> id);

	List<Guest> findAllByCustomerId(Long customerId);
	
	Optional<Guest> findById(Guest guestId);
	
	Optional<Guest> findByCustomerId(Long customerId);
	
	Guest findByPlatformUserId(String platformUserId);


}
