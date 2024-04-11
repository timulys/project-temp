/**
 *  멤버 일정 Repository
 * @수정일자	  / 수정자			 / 수정내용
 * 2023.05.17 / asher.shin	 / 신규
 */
package com.kep.portal.repository.member;

import com.kep.portal.model.entity.member.MemberSchedule;
import com.kep.portal.model.entity.member.ScheduleType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface MemberScheduleRepository  extends JpaRepository<MemberSchedule, Long> ,MemberScheduleSearchRepository{

    List<MemberSchedule> findByCompletedAndScheduleType(boolean completed, ScheduleType scheduleType);

    List<MemberSchedule> findByMemberIdAndScheduleType(Long memberId,ScheduleType scheduleType);
    List<MemberSchedule> findByMemberIdInAndScheduleTypeAndMemberIdNot(List<Long> memberIds, ScheduleType scheduleType,Long memberId);

    List<MemberSchedule> findByMemberIdAndScheduleTypeNot(Long Id,ScheduleType scheduleType);
}
