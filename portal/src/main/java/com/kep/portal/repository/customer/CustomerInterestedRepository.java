package com.kep.portal.repository.customer;

import com.kep.portal.model.dto.customer.CustomerInterestedDto;
import com.kep.portal.model.entity.customer.CustomerContact;
import com.kep.portal.model.entity.customer.CustomerInterested;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerInterestedRepository  extends JpaRepository<CustomerInterested, Long> {

    List<CustomerInterested> findByCustomerId(Long customerId);


}
