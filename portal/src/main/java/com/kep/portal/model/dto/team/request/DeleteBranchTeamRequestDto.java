package com.kep.portal.model.dto.team.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class DeleteBranchTeamRequestDto {
    @Schema(description = "삭제할 Branch Team ID 목록")
    private List<Long> branchTeamIdList;
}
