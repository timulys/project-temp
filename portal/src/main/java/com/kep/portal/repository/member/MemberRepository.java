package com.kep.portal.repository.member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kep.portal.model.entity.member.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberSearchRepository {

	Member findByUsername(String username);

	List<Member> findByIdIn(List<Long> ids);

	List<Member> findByIdNotIn(Set<Long> ids);

	List<Member> findAllByEnabled(Boolean enabled);

	List<Member> findAllByIdInAndEnabled(Collection<Long> ids, Boolean enabled);

	Page<Member> findAllByBranchIdOrderByIdDesc(Long branchId, Pageable pageable);

	List<Member> findAllByBranchIdOrderByBranchIdDesc(Long branchId);

	Page<Member> findAllByOrderByIdDesc(Pageable pageable);

	Long countByEnabled(Boolean enabled);

	List<Member> findByEnabledAndBranchId(Boolean enabled, Long branchId);
	
	
	//BNK 상담직원번호 추가
	Optional<Member> findByVndrCustNo(String vndrCustNo);
	
	// 추가된 메소드
    Long countByEnabledAndBranchId(Boolean enabled, Long branchId);
    
    //BNK 계정 카운트
    Long countByEnabledAndUsernameNot(Boolean enabled, String username);


    
    

}
