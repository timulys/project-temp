package com.kep.core.model.dto.team;

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
public class TeamMemberDto {

    @Positive
    private Long id;

    @Positive
    private Long teamId;

    @Positive
    private Long memberId;

    @NotNull
    private Long modifier;

    @NotNull
    private ZonedDateTime modified;

}
