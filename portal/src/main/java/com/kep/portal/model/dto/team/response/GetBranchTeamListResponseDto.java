package com.kep.portal.model.dto.team.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.branch.BranchTeamDto;
import com.kep.core.model.dto.common.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@ToString
public class GetBranchTeamListResponseDto extends ResponseDto {
    @Schema(description = "브랜치 팀 목록")
    private final List<BranchTeamDto> branchTeamList;

    private GetBranchTeamListResponseDto(List<BranchTeamDto> branchTeamList, String message) {
        super(ResponseCode.SUCCESS, message);
        this.branchTeamList = branchTeamList;
    }

    public static ResponseEntity<GetBranchTeamListResponseDto> success(List<BranchTeamDto> branchTeamList, String message) {
        GetBranchTeamListResponseDto result = new GetBranchTeamListResponseDto(branchTeamList, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
