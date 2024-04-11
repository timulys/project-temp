package com.kep.core.model.dto.member;


import com.kep.core.model.dto.work.WorkType;
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

    @NotNull
    private WorkType.OfficeHoursStatusType status;

}
