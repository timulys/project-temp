package com.kep.portal.repository.customer;

import com.kep.portal.model.entity.customer.CustomerCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerCategoryRepository extends JpaRepository<CustomerCategory,Long> {

}
