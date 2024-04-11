package com.kep.core.model.dto.work;

import com.kep.core.model.dto.team.TeamDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberMaxCounselDto {

    @PositiveOrZero
    private Integer maxMemberCounsel;

    @Positive
    private Long branchId;

    private List<MemberCounsel> memberCounsels;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberCounsel {
        @Positive
        @NotNull
        private Long id;

        @PositiveOrZero
        @NotNull
        private Integer number;

        private String username;
        private String nickname;
        private Integer maxCounsel;
    }

    private List<TeamDto> teams;

}
