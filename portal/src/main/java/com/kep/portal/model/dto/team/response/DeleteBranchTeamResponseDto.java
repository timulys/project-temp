package com.kep.portal.model.dto.team.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class DeleteBranchTeamResponseDto extends ResponseDto {

    private DeleteBranchTeamResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<DeleteBranchTeamResponseDto> success(String message) {
        DeleteBranchTeamResponseDto result = new DeleteBranchTeamResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
