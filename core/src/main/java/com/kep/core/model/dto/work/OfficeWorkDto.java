package com.kep.core.model.dto.work;

import com.kep.core.model.dto.branch.BranchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfficeWorkDto {

    @Positive
    @Schema(description = "브랜치 아이디")
    private Long branchId;

    @Positive
    @Schema(description = "사용자 아이디")
    private Long memberId;

    /**
     * branch , member
     */
    @Schema(description = "근무시간 유형 (branch, member)")
    private WorkType.Cases cases;

    //브랜치 설정
    @Schema(description = "브랜치 설정 정보")
    private BranchDto branch;

    //근무시간 설정
    @Schema(description = "근무시간 설정 정보")
    private OfficeHoursDto officeHours;

    @Schema(description = "근무시간 설정 정보 목록")
    private List<OfficeHoursDto> officeHoursList;

}
