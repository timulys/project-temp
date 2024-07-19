package com.kep.core.model.dto.work;

import com.kep.core.model.dto.team.TeamDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Schema(description = "사용자별 최대 상담 건수 설정")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberMaxCounselDto {

    @PositiveOrZero
    @Schema(description = "최대 상담수", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer maxMemberCounsel;

    @Positive
    @Schema(description = "브랜치 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long branchId;

    @Schema(description = "사용자별 상담 설정 정보")
    private List<MemberCounsel> memberCounsels;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberCounsel {
        @Positive
        @NotNull
        @Schema(description = "사용자 아이디")
        private Long id;

        @PositiveOrZero
        @NotNull
        @Schema(description = "횟수")
        private Integer number;

        @Schema(description = "사용자명")
        private String username;
        @Schema(description = "닉네임")
        private String nickname;
        @Schema(description = "최대 상담수")
        private Integer maxCounsel;
    }

    @Schema(description = "팀 목록")
    private List<TeamDto> teams;

}
