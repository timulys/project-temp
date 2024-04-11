package com.kep.core.model.dto.work;

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

    @Positive
    private Long id;

    @NotNull
    private List<String> dayOfWeek;

    @NotEmpty
    private String startCounselHours;

    @NotEmpty
    private String startCounselMinutes;

    @NotEmpty
    private String endCounselHours;

    @NotEmpty
    private String endCounselMinutes;

    /**
     * branch , member
     */
    private WorkType.Cases cases;

    private Long branchId;

    private Long memberId;

    @Positive
    private Long modifier;
    private ZonedDateTime modified;

    private WorkType.OfficeHoursStatusType status;

    private Boolean offDutyCounselYn;

}
