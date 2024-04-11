package com.kep.portal.repository.subject;

import com.kep.portal.model.entity.subject.IssueCategoryMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueCategoryMemberRepository extends JpaRepository<IssueCategoryMember, Long> {
}
