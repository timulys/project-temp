package com.kep.core.model.dto.work;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.List;


/**
 *  @수정일자	  / 수정자		 	/ 수정내용
 *  2023.05.31 / asher.shin / 근무외상담 컬럼 추가
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfficeHoursDto {

    @Schema(description = "근무 시간 아이디(PK)" , requiredMode = Schema.RequiredMode.REQUIRED)
    @Positive
    private Long id;

    @Schema(description = "상담 가능 요일")
    @NotNull
    private List<String> dayOfWeek;

    @Schema(description = "근무 시작 시간")
    @NotEmpty
    private String startCounselHours;

    @Schema(description = "근무 시작 분")
    @NotEmpty
    private String startCounselMinutes;


    @Schema(description = "근무 종료 시간")
    @NotEmpty
    private String endCounselHours;

    @Schema(description = "근무 종료 분")
    @NotEmpty
    private String endCounselMinutes;

    @Schema(description = "근무시간 관리방식 ( branch : 시스템 , member : 상담직원 ) ")
    private WorkType.Cases cases;

    @Schema(description = "브랜치 아이디")
    private Long branchId;

    private Long memberId;

    @Positive
    private Long modifier;
    private ZonedDateTime modified;

    private WorkType.OfficeHoursStatusType status;

    private Boolean offDutyCounselYn;

}
