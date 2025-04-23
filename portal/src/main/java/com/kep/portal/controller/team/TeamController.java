package com.kep.portal.controller.team;


import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.branch.BranchTeamDto;
import com.kep.core.model.dto.team.TeamDto;
import com.kep.portal.config.property.SystemMessageProperty;
import com.kep.portal.model.dto.team.request.PatchBranchTeamRequestDto;
import com.kep.portal.model.dto.team.request.PostBranchTeamRequestDto;
import com.kep.portal.model.dto.team.response.GetTeamListResponseDto;
import com.kep.portal.model.dto.team.response.PatchBranchTeamResponseDto;
import com.kep.portal.model.dto.team.response.PostBranchTeamResponseDto;
import com.kep.portal.service.branchTeam.aggregation.BranchTeamServiceAggregation;
import com.kep.portal.service.team.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Tag(name = "팀 관리 API", description = "/api/v1/team")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team")
public class TeamController {
    // Autowired Components
    private final TeamService teamService;
    private final BranchTeamServiceAggregation branchTeamServiceAggregation;

    /**
     * 매니저가 그룹장인 상담그룹의 계정목록 조회
     * ※ 현재는 상담지원요청 목록을 매니저가 조회 시 해당 부분을 사용함
     * @return
     */
    @Tag(name = "팀 관리 API")
    @Operation(summary = "매니저가 그룹장인 상담그룹의 계정목록 조회", description = "※ 현재는 상담지원요청 목록을 매니저가 조회 시 해당 부분을 사용함")
    @GetMapping("/group/members")
    public ResponseEntity<ApiResult<List<BranchTeamDto>>> groupMembers() {
        log.info("TEAM GROUP MEMBERS, GET");
        ApiResult<List<BranchTeamDto>> response = ApiResult.<List<BranchTeamDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(teamService.getTeamGroupMembers())
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    /**
     * FIXME :: 기능 구현 안되어 있음(삭제 요건 정의되어 있지 않은듯) 20240715 volka
     *
     * 팀 회원 삭제
     * @param id
     * @param teamDto
     * @return
     * @throws Exception
     */
    @Tag(name = "팀 관리 API")
    @Operation(summary = "팀 사용자 삭제(미구현::요건 미정의)")
    @DeleteMapping(value = "/{id}/member")
    public ResponseEntity<ApiResult<TeamDto>> memberRemove(
            @PathVariable("id") Long id
            , @RequestBody @NotNull TeamDto teamDto) throws Exception {

//        teamService.memberDelete(teamDto , id);
        ApiResult<TeamDto> response = ApiResult.<TeamDto>builder()
                .code(ApiResultCode.succeed)
                .build();

        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    /**
     * 팀 수정
     * @param dto
     * @return
     * @throws Exception
     */
    @Tag(name = "팀 관리 API")
    @Operation(summary = "팀 수정")
    @PutMapping(value = {"/{id}", "/modify/{id}"})
    public ResponseEntity<ApiResult<BranchTeamDto>> modify(
            @Parameter(description = "팀 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long teamId ,
            @RequestBody @NotNull TeamDto dto) throws Exception {

        dto.setId(teamId);
        log.info("TEAM, PUT, BODY: {}" , dto);
        ApiResult<BranchTeamDto> response = ApiResult.<BranchTeamDto>builder()
                .code(ApiResultCode.succeed)
                .payload(teamService.store(dto))
                .build();

        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    /**
     * 팀 조회
     * @param id
     * @return
     */
    @Deprecated
    @Tag(name = "팀 관리 API")
    @Operation(summary = "팀 조회")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResult<BranchTeamDto>> get(
            @Parameter(description = "팀 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable("id") Long id) {

        ApiResult<BranchTeamDto> response = ApiResult.<BranchTeamDto>builder()
                .code(ApiResultCode.succeed)
                .payload(teamService.get(id))
                .build();

        return new ResponseEntity<>(response , HttpStatus.OK);

    }

    /**
     *
     * @param ids
     * @return
     */
    @Tag(name = "팀 관리 API")
    @Operation(summary = "팀 삭제")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResult<List<BranchTeamDto>>> deleteTeam(
            @Parameter(description = "팀 아이디 목록", in = ParameterIn.PATH, required = true)
            @PathVariable("id") Long[] ids
    ) {
        Boolean delete = teamService.delete(ids);
        if(delete){
            log.info("TEAM, DELETED");
            ApiResult<List<BranchTeamDto>> response = ApiResult.<List<BranchTeamDto>>builder()
                    .code(ApiResultCode.succeed)
                    .build();
            return new ResponseEntity<>(response , HttpStatus.OK);
        }else{
            log.info("TEAM, NOT DELETED");
            ApiResult<List<BranchTeamDto>> response = ApiResult.<List<BranchTeamDto>>builder()
                    .code(ApiResultCode.failed)
                    .build();
            return new ResponseEntity<>(response , HttpStatus.OK);
        }
    }

    /** V2 Apis **/
    @Operation(summary = "상담그룹 추가(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = PostBranchTeamResponseDto.class)))
    @PostMapping
    public ResponseEntity<? super PostBranchTeamResponseDto> postBranchTeam(
            @RequestBody @Valid PostBranchTeamRequestDto requestBody) {
        log.info("Create New Branch Team, Request: {}", requestBody);
        ResponseEntity<? super PostBranchTeamResponseDto> response = branchTeamServiceAggregation.postBranchTeam(requestBody);
        log.info("Create New Branch Team, Response: {}", response);
        return response;
    }

    @Operation(summary = "상담 배분 설정 > 상담직원 조회(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetTeamListResponseDto.class)))
    @GetMapping("/members/{channelId}")
    public ResponseEntity<? super GetTeamListResponseDto> getTeamMembersWithChannelId(
            @Parameter(description = "채널 아이디") @PathVariable("channelId") Long channelId) {
        log.info("Get Team With Members, Channel ID : {}", channelId);
        ResponseEntity<? super GetTeamListResponseDto> response = teamService.getTeamMembersWithChannelId(channelId);
        log.info("Get Team With Members, Response : {}", response);
        return response;
    }

    @Operation(summary = "상담그룹 수정(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = PatchBranchTeamResponseDto.class)))
    @PatchMapping
    public ResponseEntity<? super PatchBranchTeamResponseDto> patchBranchTeam(
            @RequestBody @Valid PatchBranchTeamRequestDto requestBody) {
        log.info("Patch Branch Team, Request: {}", requestBody);
        ResponseEntity<? super PatchBranchTeamResponseDto> response = branchTeamServiceAggregation.patchBranchTeam(requestBody);
        log.info("Patch Branch Team, Response: {}", response);
        return response;
    }
}
