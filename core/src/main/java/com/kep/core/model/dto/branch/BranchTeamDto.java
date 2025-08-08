package com.kep.core.model.dto.branch;

import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.team.TeamDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchTeamDto {
    @Positive
    @Schema(description = "브랜치 팀 아이디")
    private Long id;
    @Schema(description = "브랜치 정보")
    private BranchDto branch;
    @Schema(description = "팀 정보")
    private TeamDto team;
    @Schema(description = "사용자 정보")
    private MemberDto member;
}
