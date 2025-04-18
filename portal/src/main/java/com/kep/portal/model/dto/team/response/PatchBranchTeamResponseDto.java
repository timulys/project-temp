package com.kep.portal.model.dto.team.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class PatchBranchTeamResponseDto extends ResponseDto {
    private PatchBranchTeamResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<PatchBranchTeamResponseDto> success(String message) {
        PatchBranchTeamResponseDto result = new PatchBranchTeamResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
