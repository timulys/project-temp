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

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "사용자 스케줄")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberScheduleDto {

    @Positive
    @Schema(description = "사용자 스케줄 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @JsonIncludeProperties({"id", "name","contacts"})
    @Schema(description = "고객 정보")
    private CustomerDto customer;

    @NotNull
    @Schema(description = "제목", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "내용")
    private String content;

    @NotNull
    @Schema(description = "스케줄 타입(holiday,meeting,telephone,etc)", requiredMode = Schema.RequiredMode.REQUIRED)
    private ScheduleType scheduleType;

    @Schema(description = "주소")
    private String address;

    @Schema(description = "상세 주소")
    private String addressDetail;

    @JsonFormat(pattern = "yyyy-MM-dd" ,shape = JsonFormat.Shape.STRING ,timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @Schema(description = "시작일 (yyyy-MM-dd)", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate startDate;

    @JsonFormat(pattern = "HH:mm:ss" ,shape = JsonFormat.Shape.STRING ,timezone = "Asia/Seoul")
    @NotNull
    @Schema(description = "시작 시간 (HH:mm:ss)", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime startTime;


    @JsonFormat(pattern = "yyyy-MM-dd" ,shape = JsonFormat.Shape.STRING ,timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @Schema(description = "")
    private LocalDate endDate;

    @JsonFormat(pattern = "HH:mm:ss" ,shape = JsonFormat.Shape.STRING ,timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @NotNull
    @Schema(description = "")
    private LocalTime endTime;

    @JsonIncludeProperties({"id", "nickname"})
    @Schema(description = "사용자 정보")
    private MemberDto member;

    @Schema(description = "알림 메시지 여부")
    private Boolean alarmMessageYn;

    @Schema(description = "10분전 알림 여부")
    private Boolean beforeTenAlarmMessageYn;

    @Schema(description = "알림 템플릿 아이디")
    private Long alarmTemplateId;

    @Schema(description = "10분전 알림 템플릿 아이디")
    private Long beforeTenAlarmTemplateId;

    @Schema(description = "완료여부", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean completed;

    @Schema(description = "고객 아이디")
    private Long customerId;

    @Schema(description = "사용자 아이디")
    private Long memberId;

    public static class MemberScheduleDtoBuilder {}

}
