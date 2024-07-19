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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OffDutyHoursDto {

    @Positive
    @Schema(description = "브랜치 근무시간 예외 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotNull
    @Schema(description = "사용여부", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean enabled;

    @NotEmpty
    @Schema(description = "시작일시", requiredMode = Schema.RequiredMode.REQUIRED)
    private String startCreated;

    @NotEmpty
    @Schema(description = "종료일시", requiredMode = Schema.RequiredMode.REQUIRED)
    private String endCreated;

    @NotEmpty
    @Schema(description = "내용", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contents;

    /**
     * branch , member
     */
    @Schema(description = "근무시간 유형(branch , member)")
    private WorkType.Cases cases;

    @Schema(description = "브랜치 아이디")
    private Long branchId;

    @Schema(description = "사용자 아이디")
    private Long memberId;

    @Positive
    @Schema(description = "수정자")
    private Long modifier;
    @Schema(description = "수정일시")
    private ZonedDateTime modified;
}
