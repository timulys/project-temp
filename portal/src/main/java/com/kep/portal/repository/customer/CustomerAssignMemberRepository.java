//package com.kep.portal.repository.customer;
//
//import com.kep.portal.model.entity.customer.Customer;
//import com.kep.portal.model.entity.customer.CustomerAssignMember;
//import org.springframework.data.jpa.repository.EntityGraph;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//
// TODO :: 지정 담당자 / 서브 담당자 요건 적용 시 주석 제거 20250204 volka
//
//@Repository
//public interface CustomerAssignMemberRepository extends JpaRepository<CustomerAssignMember, Integer> {
//
//    @EntityGraph(attributePaths = {"customer", "mainMember", "subMember"})
//    Optional<CustomerAssignMember> findByCustomer(Customer customer);
//}
