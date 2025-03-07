package com.kep.portal.repository.customer;

import com.kep.portal.model.entity.customer.CustomerMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerMemberRepository extends JpaRepository<CustomerMember, Long> {


    List<CustomerMember> findAllByMemberIdAndFavorite(Long memberId , boolean favorite);
    Page<CustomerMember> findAllByMemberId(Long memberId , Pageable pageable);


    void deleteByCustomerId(Long customerId);
}
