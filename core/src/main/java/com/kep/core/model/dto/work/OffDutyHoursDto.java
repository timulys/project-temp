package com.kep.core.model.dto.work;

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
    private Long id;

    @NotNull
    private Boolean enabled;

    @NotEmpty
    private String startCreated;

    @NotEmpty
    private String endCreated;

    @NotEmpty
    private String contents;

    /**
     * branch , member
     */
    private WorkType.Cases cases;

    private Long branchId;

    private Long memberId;

    @Positive
    private Long modifier;
    private ZonedDateTime modified;
}
