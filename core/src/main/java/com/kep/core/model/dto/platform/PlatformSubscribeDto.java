package com.kep.core.model.dto.platform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformSubscribeDto {

    @Positive
    @Schema(description = "플랫폼 구독 아이디")
    private Long id;
    @Schema(description = "서비스 아이디")
    private String serviceId;
    @Schema(description = "플랫폼 사용자 아이디")
    private String platformUserId;
    @Schema(description = "사용여부")
    private Boolean enabled;
    @Schema(description = "수정일시")
    private ZonedDateTime modified;
}
