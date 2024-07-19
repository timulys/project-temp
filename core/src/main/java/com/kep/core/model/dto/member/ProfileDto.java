package com.kep.core.model.dto.member;


import com.kep.core.model.dto.work.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {

    @Schema(description = "근무 상태(on : 근무, off : 오프, rest : 휴식, meal : 식사시간)")
    @NotNull
    private WorkType.OfficeHoursStatusType status;

}
