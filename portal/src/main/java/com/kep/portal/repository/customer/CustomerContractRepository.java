package com.kep.portal.repository.customer;

import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.CustomerContract;
import com.kep.portal.model.entity.customer.CustomerInterested;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerContractRepository extends JpaRepository<CustomerContract,Long> {

    List<CustomerContract> findByCustomer(Customer customer);
}
