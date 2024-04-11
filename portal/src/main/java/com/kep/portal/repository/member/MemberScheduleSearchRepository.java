package com.kep.portal.repository.member;

import com.kep.portal.model.dto.member.MemberScheduleDto;
import com.kep.portal.model.entity.member.MemberSchedule;
import com.kep.portal.model.entity.member.ScheduleType;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface MemberScheduleSearchRepository {
    List<MemberScheduleDto> getMyScheduleList(Long memberId, LocalDate startDate, LocalDate endDate);

    List<MemberScheduleDto> getMyScheduleTypeList(Long memberId, ScheduleType scheduleType,LocalDate startDate, LocalDate endDate);
    List<MemberScheduleDto> getTeamScheduleList(List<Long> memberIds, ScheduleType scheduleType, Long memberId, LocalDate startDate,LocalDate endDate);

    List<MemberScheduleDto> getAlarmMessageList(Long sendType,List<ScheduleType> scheduleTypes, LocalDate startDate,LocalDate endDate);

    List<MemberSchedule> search(Long memberId , LocalDate start , LocalDate end , Set<ScheduleType> types , Boolean completed , List<Long> customerId);

}
