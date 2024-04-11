package com.kep.core.model.dto.platform;

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
    private Long id;
    private String serviceId;
    private String platformUserId;
    private Boolean enabled;
    private ZonedDateTime modified;
}
