package com.kep.portal.service.branchTeam;

import com.kep.core.model.dto.branch.BranchTeamDto;
import com.kep.portal.model.dto.team.request.PatchBranchTeamRequestDto;
import com.kep.portal.model.dto.team.request.PostBranchTeamRequestDto;

public interface BranchTeamService {
    // Response Entity Methods

    // Aggregation Methods
    BranchTeamDto saveBranchTeam(PostBranchTeamRequestDto dto, Long teamId);
    BranchTeamDto modifyBranchTeam(PatchBranchTeamRequestDto dto);
}
