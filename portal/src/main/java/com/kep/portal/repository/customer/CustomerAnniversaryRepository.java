package com.kep.portal.repository.customer;

import com.kep.portal.model.entity.customer.CustomerAnniversary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Repository
public interface CustomerAnniversaryRepository extends JpaRepository<CustomerAnniversary, Long> {
    void deleteByCustomerId(@NotNull @Positive Long customerId);

    List<CustomerAnniversary> findAllByCustomerId(@NotNull @Positive Long customerId);
}
