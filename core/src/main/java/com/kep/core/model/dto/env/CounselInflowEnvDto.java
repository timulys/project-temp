package com.kep.core.model.dto.env;

import com.kep.core.model.dto.system.SystemEnvEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "상담 유입경로 아이디(PK)")
    @Positive
    private Long id;

    @Schema(description = "브랜치 아이디")
    @Positive
    private Long branchId;

    @Schema(description = "상담 유입경로")
    @NotNull
    private String params;

    @Schema(description = "상담 유입경로 명")
    @NotNull
    private String name;

    @Schema(description = "상담 유입경로 대상 ( official : 공식 채널 , unlimited : 제한없음 )")
    @NotNull
    private SystemEnvEnum.InflowPathType inflowPathType;

    @Schema(description = "상담 유입경로 값")
    private String value;

    @Schema(description = "수정자")
    private Long modifier;

    @Schema(description = "수정일")
    private ZonedDateTime modified;


}
