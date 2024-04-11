package com.kep.core.model.dto.env;

import com.kep.core.model.dto.system.SystemEnvEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CounselInflowEnvDto {

    @Positive
    private Long id;

    @Positive
    private Long branchId;

    @NotNull
    private String params;

    @NotNull
    private String name;

    @NotNull
    private SystemEnvEnum.InflowPathType inflowPathType;

    private String value;

    private Long modifier;

    private ZonedDateTime modified;


}
