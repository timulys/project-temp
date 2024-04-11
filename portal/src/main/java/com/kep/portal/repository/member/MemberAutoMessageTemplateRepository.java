package com.kep.portal.repository.member;

import com.kep.portal.model.entity.member.MemberAutoMessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberAutoMessageTemplateRepository extends JpaRepository<MemberAutoMessageTemplate,Long> {
    List<MemberAutoMessageTemplate> findByCategory(int category);
}
