package com.kep.core.model.dto.work;

import com.kep.core.model.dto.branch.BranchDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfficeWorkDto {

    @Positive
    private Long branchId;

    @Positive
    private Long memberId;

    /**
     * branch , member
     */
    private WorkType.Cases cases;

    //브랜치 설정
    private BranchDto branch;

    //근무시간 설정
    private OfficeHoursDto officeHours;

    private List<OfficeHoursDto> officeHoursList;

}
