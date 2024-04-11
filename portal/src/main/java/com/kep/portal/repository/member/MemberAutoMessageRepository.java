package com.kep.portal.repository.member;

import com.kep.portal.model.entity.member.MemberAutoMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAutoMessageRepository extends JpaRepository< MemberAutoMessage,Long> {
}
