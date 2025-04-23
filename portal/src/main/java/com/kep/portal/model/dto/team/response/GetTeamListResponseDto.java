package com.kep.portal.model.dto.team.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.team.TeamDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@ToString
public class GetTeamListResponseDto extends ResponseDto {
    @Schema(description = "팀 목록")
    private final List<TeamDto> teamList;

    private GetTeamListResponseDto(List<TeamDto> teamList, String message) {
        super(ResponseCode.SUCCESS, message);
        this.teamList = teamList;
    }

    public static ResponseEntity<GetTeamListResponseDto> success(List<TeamDto> teamList, String message) {
        GetTeamListResponseDto result = new GetTeamListResponseDto(teamList, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
