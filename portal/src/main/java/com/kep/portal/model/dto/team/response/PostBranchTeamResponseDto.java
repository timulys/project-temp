package com.kep.portal.model.dto.team.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.branch.BranchTeamDto;
import com.kep.core.model.dto.common.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class PostBranchTeamResponseDto extends ResponseDto {
    @Schema(description = "상담그룹")
    private final BranchTeamDto branchTeam;

    private PostBranchTeamResponseDto(BranchTeamDto branchTeam, String message) {
        super(ResponseCode.SUCCESS, message);
        this.branchTeam = branchTeam;
    }

    public static ResponseEntity<PostBranchTeamResponseDto> success(BranchTeamDto branchTeam, String message) {
        PostBranchTeamResponseDto result = new PostBranchTeamResponseDto(branchTeam, message);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
