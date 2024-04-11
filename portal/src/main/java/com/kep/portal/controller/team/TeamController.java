package com.kep.portal.controller.team;


import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.branch.BranchTeamDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.team.TeamDto;
import com.kep.core.model.exception.BizException;
import com.kep.portal.service.team.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/team")
@Slf4j
public class TeamController {

    @Resource
    private TeamService teamService;

    /**
     * 상담 관리 > 상담 이력 > 검색, SB-CA-003
     * 상담 관리 > 상담 진행 목록 > 검색, SB-CA-002
     * 계정 관리 > 상담 그룹 관리 > 목록, SB-SA-P01
     */
    @GetMapping
    public ResponseEntity<ApiResult<List<BranchTeamDto>>> get(
            @SortDefault.SortDefaults({
                    @SortDefault(sort = {"id"}, direction = Sort.Direction.ASC)}) Pageable pageable) {

        log.info("TEAM, GET ALL");
        ApiResult<List<BranchTeamDto>> response = ApiResult.<List<BranchTeamDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(teamService.getAll())
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    /**
     * 목록 조회시 응답
     */
    private ResponseEntity<ApiResult<List<TeamDto>>> response(@NotNull Page<TeamDto> page, @NotNull HttpStatus httpStatus) {

        ApiResult<List<TeamDto>> response = ApiResult.<List<TeamDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(page.getContent())
                .totalPage(page.getTotalPages())
                .totalElement(page.getTotalElements())
                .currentPage(page.getNumber())
                .build();
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * 매니저가 그룹장인 상담그룹의 계정목록 조회
     * ※ 현재는 상담지원요청 목록을 매니저가 조회 시 해당 부분을 사용함
     * @return
     */
    @GetMapping("/group/members")
    public ResponseEntity<ApiResult<List<BranchTeamDto>>> groupMembers() {
        log.info("TEAM GROUP MEMBERS, GET");
        ApiResult<List<BranchTeamDto>> response = ApiResult.<List<BranchTeamDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(teamService.getTeamGroupMembers())
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @GetMapping(value = "/with-members")
    public ResponseEntity<ApiResult<List<TeamDto>>> getWithMembers(
            @SortDefault.SortDefaults({
                    @SortDefault(sort = {"id"}, direction = Sort.Direction.ASC)}) Pageable pageable) {

        log.info("TEAM, GET ALL WITH MEMBERS");

        Page<TeamDto> teams = teamService.getAllWithMembers(pageable , null);
        return response(teams, HttpStatus.OK);
    }

    /**
     * 팀 회원 삭제
     * @param id
     * @param teamDto
     * @return
     * @throws Exception
     */
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
     * 팀 추가
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping(value = {"", "/create"})
    public ResponseEntity<ApiResult<BranchTeamDto>> create(
            @RequestBody @NotNull TeamDto dto) throws Exception {

        log.info("TEAM, POST, BODY: {}" , dto);

        BranchTeamDto branchTeamDto = null;
        try {
            branchTeamDto = teamService.store(dto);
        } catch (DataIntegrityViolationException e) {
            if(e.getLocalizedMessage().contains("UK_TEAM___NAME")){
                Map<String, Object> extra = new HashMap<>();
                extra.put("name",dto.getName());
                throw new BizException("SB-SA-P01-001", "SB-SA-P01-001" , extra);
            } else {
                throw new BizException();
            }
        }

        ApiResult<BranchTeamDto> response = ApiResult.<BranchTeamDto>builder()
                .code(ApiResultCode.succeed)
                .payload(branchTeamDto)
                .build();

        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }

    /**
     * 팀 수정
     * @param dto
     * @return
     * @throws Exception
     */
    @PutMapping(value = {"/{id}", "/modify/{id}"})
    public ResponseEntity<ApiResult<BranchTeamDto>> modify(
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
    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResult<BranchTeamDto>> get(
            @PathVariable("id") Long id) {

        ApiResult<BranchTeamDto> response = ApiResult.<BranchTeamDto>builder()
                .code(ApiResultCode.succeed)
                .payload(teamService.get(id))
                .build();

        return new ResponseEntity<>(response , HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResult<List<BranchTeamDto>>> deleteTeam(@PathVariable("id") Long[] ids) {
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
}
