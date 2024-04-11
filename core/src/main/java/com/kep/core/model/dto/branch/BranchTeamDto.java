package com.kep.core.model.dto.branch;

import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.team.TeamDto;
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
    private Long id;
    private BranchDto branch;
    private List<TeamDto> teams;
    private TeamDto team;
    private MemberDto member;

}
