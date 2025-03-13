package com.kep.portal.repository.customer;

import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.CustomerGroup;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Deprecated
	List<Customer> findAllByNameContaining(@NotEmpty String name);

	List<Customer> findAllByName(@NotEmpty String name);
	
	List<Customer> findAllByCustomerGroup(CustomerGroup customerGroup);
	/**
	 * 고객 이메일 정보
	 * @param identifier
	 * @return
	 */
	Customer findByIdentifier(@NotEmpty String identifier);

    List<Customer> findAllByIdIn(List<Long> guestId);

    Customer findAllById(Long Id);

	Optional<Customer> findById(Long Id);

	@Query("SELECT c FROM Customer c " +
			"JOIN CustomerMember cm ON c.id = cm.customer.id " +
			"JOIN CustomerAnniversary ca ON c.id = ca.customerId " +
			"WHERE cm.memberId = :memberId " +
			"AND FUNCTION('DATE_FORMAT', ca.anniversary, '%m-%d') " +
			"BETWEEN FUNCTION('DATE_FORMAT', :startDate, '%m-%d') " +
			"AND FUNCTION('DATE_FORMAT', :endDate, '%m-%d') " +
			"ORDER BY ca.anniversary ASC")
	List<Customer> findCustomersWithAnniversaries(@Param("memberId") Long memberId,
												  @Param("startDate") LocalDate startDate,
												  @Param("endDate") LocalDate endDate);
}
