package com.kep.portal.repository.work;

import com.kep.portal.model.entity.work.MemberOffDutyHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface MemberOffDutyHoursRepository extends JpaRepository<MemberOffDutyHours, Long> {

    MemberOffDutyHours findByMemberId(Long memberId);
    List<MemberOffDutyHours> findAllByMemberId(Long memberId);

    List<MemberOffDutyHours> findAllByMemberIdInAndStartCreatedAfterAndEndCreatedBefore(List<Long> memberIds
            , ZonedDateTime startCreated
            , ZonedDateTime endCreated);

}
