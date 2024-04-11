/**
 *  멤버 일정 Dto
 * @수정일자	  / 수정자			 / 수정내용
 * 2023.05.17 / asher.shin	 / 신규
 */
package com.kep.portal.model.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.portal.model.entity.member.ScheduleType;

import javax.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberScheduleDto {

    @Positive
    private Long id;

    @JsonIncludeProperties({"id", "name","contacts"})
    private CustomerDto customer;

    @NotNull
    private String title;

    private String content;

    @NotNull
    private ScheduleType scheduleType;

    private String address;

    private String addressDetail;

    @JsonFormat(pattern = "yyyy-MM-dd" ,shape = JsonFormat.Shape.STRING ,timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate startDate;

    @JsonFormat(pattern = "HH:mm:ss" ,shape = JsonFormat.Shape.STRING ,timezone = "Asia/Seoul")
    @NotNull
    private LocalTime startTime;


    @JsonFormat(pattern = "yyyy-MM-dd" ,shape = JsonFormat.Shape.STRING ,timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate endDate;

    @JsonFormat(pattern = "HH:mm:ss" ,shape = JsonFormat.Shape.STRING ,timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @NotNull
    private LocalTime endTime;

    @JsonIncludeProperties({"id", "nickname"})
    private MemberDto member;

    private Boolean alarmMessageYn;

    private Boolean beforeTenAlarmMessageYn;

    private Long alarmTemplateId;

    private Long beforeTenAlarmTemplateId;

    private boolean completed;

    private Long customerId;

    private Long memberId;

    public static class MemberScheduleDtoBuilder {}

}
