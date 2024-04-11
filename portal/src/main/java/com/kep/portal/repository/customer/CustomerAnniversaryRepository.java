package com.kep.portal.repository.customer;

import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.CustomerAnniversary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

@Repository
public interface CustomerAnniversaryRepository extends JpaRepository<CustomerAnniversary, Long> {
    void deleteByCustomerId(@NotNull @Positive Long customerId);

    List<CustomerAnniversary> findAllByCustomerId(@NotNull @Positive Long customerId);

//    검색을 사용할 이유가 없을거 같아서 주석
//    @Query(value = "SELECT e FROM CustomerAnniversary e WHERE e.customerId IN (:customerId) AND MONTH(e.anniversary) BETWEEN :startMonth AND :endMonth AND DAY_OF_MONTH(e.anniversary) BETWEEN:startDay AND :endDay ORDER BY e.anniversary ASC")
//    List<CustomerAnniversary> findAllAnniversary(@Param("customerId") Set<Long> customerId
//            , @Param("startMonth") int startMonth
//            , @Param("endMonth") int endMonth
//            , @Param("startDay") int startDay
//            , @Param("endDay") int endDay);
}
