package com.kep.portal.repository.customer;

import com.kep.portal.model.entity.customer.CustomerGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Long> {
    List<CustomerGroup> findAllByMemberId(Long memberId);
    boolean existsByGroupName(String groupName);
}
