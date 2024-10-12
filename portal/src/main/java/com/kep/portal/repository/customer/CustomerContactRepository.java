package com.kep.portal.repository.customer;

import com.kep.core.model.dto.customer.CustomerContactType;
import com.kep.portal.model.entity.customer.CustomerContact;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

public interface CustomerContactRepository extends JpaRepository<CustomerContact, Long> {
    void deleteByCustomerId(@NotNull @Positive Long customerId);
    List<CustomerContact> findAllByCustomerId(@NotNull @Positive Long customerId);
    List<CustomerContact> findCustomerContactsByType(CustomerContactType type);
    List<CustomerContact> findAllByCustomerIdIn(List<Long> customerIds);
}
