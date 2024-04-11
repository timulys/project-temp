package com.kep.portal.repository.assign;

import com.kep.portal.model.entity.work.MemberOfficeHours;
import com.kep.portal.model.entity.work.OfficeHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MemberOfficeHoursRepository extends JpaRepository<MemberOfficeHours, Long> {

    MemberOfficeHours findByMemberId(Long memberId);

    List<MemberOfficeHours> findAllByMemberIdIn(Collection<Long> memberIds);
}
