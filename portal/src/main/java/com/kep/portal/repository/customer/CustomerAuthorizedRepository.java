package com.kep.portal.repository.customer;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kep.portal.model.entity.customer.CustomerAuthorized;
@Repository
public interface CustomerAuthorizedRepository extends JpaRepository<CustomerAuthorized, Long> {
    void deleteByCustomerId(@NotNull @Positive Long customerId);

    List<CustomerAuthorized> findAllByCustomerId(@NotNull @Positive Long customerId);

    List<CustomerAuthorized> findAllByCustomerIdIn(@NotNull Set<Long> customerId);
    
    CustomerAuthorized findByPlatformUserId(@NotNull Object platformUserId);
    
    List<CustomerAuthorized> findByPlatformUserIdOrderByIdDesc(@NotNull Object platformUserId);

    List<CustomerAuthorized> findByCustomerIdOrderByIdDesc(@NotNull Long customerId);
}
