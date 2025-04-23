package com.kep.portal.controller.branch;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.branch.BranchChannelDto;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.branch.BranchRoleDto;
import com.kep.core.model.dto.branch.BranchTeamDto;
import com.kep.core.model.dto.env.CounselEnvDto;
import com.kep.core.model.dto.env.CounselInflowEnvDto;
import com.kep.core.model.dto.work.OfficeHoursDto;
import com.kep.core.model.dto.work.OfficeWorkDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.core.model.exception.BizException;
import com.kep.portal.config.property.SystemMessageProperty;
import com.kep.portal.model.dto.branch.BranchDtoWithRole;
import com.kep.portal.model.dto.branch.request.PatchBranchRequestDto;
import com.kep.portal.model.dto.branch.request.PostBranchRequestDto;
import com.kep.portal.model.dto.branch.response.PatchBranchResponseDto;
import com.kep.portal.model.dto.branch.response.PostBranchResponseDto;
import com.kep.portal.model.dto.guestMemo.response.PostGuestMemoResponseDto;
import com.kep.portal.model.dto.team.TeamMembersDto;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.service.branch.BranchManageService;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.branch.aggregation.BranchServiceAggregation;
import com.kep.portal.service.env.CounselEnvService;
import com.kep.portal.service.team.TeamService;
import com.kep.portal.service.work.OfficeHoursService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 브랜치
 */
@Tag(name = "브랜치 API", description = "/api/v1/branch")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/branch")
public class BranchController {
    // Autowired Components
    private final TeamService teamService;
    private final BranchService branchService;
    private final CounselEnvService counselEnvService;
    private final OfficeHoursService officeHoursService;
    private final BranchManageService branchManageService;
    private final BranchServiceAggregation branchServiceAggregation;

    private final SystemMessageProperty systemMessageProperty;
//    /**
//     * 활성 / 비활성
//     * @param id
//     * @return
//     */
//    @Deprecated // TODO : 사용 유무 체크 후 불필요 API 면 삭제
//    @Tag(name = "브랜치 API")
//    @Operation(summary = "브랜치 활성/비활성", description = "브랜치 활성/비활성 처리 API")
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<ApiResult<BranchDto>> activation(
//            @Parameter(in = ParameterIn.PATH, description = "아이디")
//            @PathVariable("id") Long id,
//            @RequestBody BranchDto dto) {
//
//        boolean enabled = (dto.getEnabled() != null ? dto.getEnabled() : true);
//        dto = branchService.enabled(id,enabled);
//
//        ApiResultCode result = ApiResultCode.failed;
//        if(enabled == dto.getEnabled()){
//            result = ApiResultCode.succeed;
//        }
//
//        ApiResult<BranchDto> response = ApiResult.<BranchDto>builder()
//                .code(result)
//                .payload(dto)
//                .build();
//        return new ResponseEntity<>(response , HttpStatus.ACCEPTED);
//
//    }
//
//    /**
//     * 브랜치 생성
//     * @param branchDto
//     * @return
//     * @throws Exception
//     */
//    @Deprecated // 사용하지 않는 API 인지 확인 후 제거
//    @PostMapping
//    @Tag(name = "브랜치 API")
//    @Operation(summary = "브랜치 생성", description = "브랜치 생성")
//    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
//    public ResponseEntity<ApiResult<BranchDto>> create(@RequestBody @Valid BranchDto branchDto) {
//        try {
//            BranchDto branch =  branchService.store(branchDto);
//            ApiResult<BranchDto> response = ApiResult.<BranchDto>builder().code(ApiResultCode.succeed)
//                    .payload(branch)
//                    .build();
//            return new ResponseEntity<>(response , HttpStatus.CREATED);
//        } catch (DataIntegrityViolationException e) {
//            log.info("BRANCH DUPLICATION ERROR :  ", e );
//            throw new BizException(systemMessageProperty.getValidation().getDuplication().getBranch());
//        } catch (Exception exception){
//            log.info("BRANCH Create Exception :  ", exception );
//            throw new BizException(exception.getMessage());
//        }
//    }
//
//    /**
//     * 브랜치 수정
//     * @param branchId
//     * @param branchDto
//     * @return
//     * @throws Exception
//     */
//    @PutMapping(value = "/{id}")
//    @Tag(name = "브랜치 API")
//    @Operation(summary = "브랜치 수정", description = "브랜치 수정")
//    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
//    public ResponseEntity<ApiResult<BranchDto>> update (
//            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH)
//            @PathVariable(name = "id") Long branchId,
//            @RequestBody @Valid BranchDto branchDto) {
//        branchDto.setId(branchId);
//        BranchDto branch = branchService.store(branchDto);
//        ApiResult<BranchDto> response = ApiResult.<BranchDto>builder()
//                .code(ApiResultCode.succeed)
//                .payload(branch)
//                .build();
//
//        return new ResponseEntity<>(response , HttpStatus.ACCEPTED);
//    }
    
    /**
     * branch teams
     * @param branchId
     * @return
     */
    @Tag(name = "브랜치 API")
    @Operation(summary = "브랜치 아이디로 팀 목록 조회", description = "브랜치 아이디로 팀 목록 조회")
    @GetMapping(value = "/{id}/team")
    public ResponseEntity<ApiResult<List<BranchTeamDto>>> teams(
            @Parameter(in = ParameterIn.PATH, description = "브랜치 아이디", required = true)
            @PathVariable(name = "id") @NotNull Long branchId,
            @SortDefault.SortDefaults({
                    @SortDefault(sort = {"id"}, direction = Sort.Direction.ASC)}) Pageable pageable) {

        Page<BranchTeamDto> items = teamService.getAll(pageable,branchId);
        ApiResult<List<BranchTeamDto>> response = ApiResult.<List<BranchTeamDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(items.getContent())
                .currentPage(items.getNumber())
                .totalPage(items.getTotalPages())
                .totalElement(items.getTotalElements())
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    /**
     * 브랜치 팀 회원 목록
     * @param branchId
     * @return
     */
    @Tag(name = "브랜치 API")
    @Operation(summary = "브랜치 팀 회원 목록 조회", description = "브랜치 팀 내 회원 목록 조회")
    @GetMapping(value = "/{id}/team/member")
    public ResponseEntity<ApiResult<List<TeamMembersDto>>> teamMemberList(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long branchId
            , @Parameter(description = "닉네임", in = ParameterIn.QUERY)
            @RequestParam(name = "nickname", required = false , defaultValue = "") String nickName
            , @Parameter(description = "팀 아이디", in = ParameterIn.QUERY)
            @RequestParam(name = "team_id", required = false , defaultValue = "0") Long teamId) {

        List<Team> teamList = branchService.branchHasManyTeam(branchId , teamId);
        List<TeamMembersDto> teamHasManyMembers = teamService.teamHasManyMembers(teamList , nickName);

        ApiResult<List<TeamMembersDto>> response = ApiResult.<List<TeamMembersDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(teamHasManyMembers)
                .build();

        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @Tag(name = "브랜치 API")
    @Operation(summary = "브랜치 목록 조회", description = "브랜치 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResult<List<BranchDto>>> get(Pageable pageable) {
        Page<BranchDto> items = branchService.getAll(pageable);
        ApiResult<List<BranchDto>> response = ApiResult.<List<BranchDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(items.getContent())
                .currentPage(items.getNumber())
                .totalPage(items.getTotalPages())
                .totalElement(items.getTotalElements())
                .build();

        return new ResponseEntity<>(response , HttpStatus.OK);

    }

    @Tag(name = "브랜치 API")
    @Operation(summary = "브랜치 단건 조회", description = "브랜치 단건 조회")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResult<BranchDto>> get(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH)
            @PathVariable("id") Long id) {

        ApiResult<BranchDto> response = ApiResult.<BranchDto>builder()
                .code(ApiResultCode.succeed)
                .payload(branchService.getById(id))
                .build();

        return new ResponseEntity<>(response , HttpStatus.OK);

    }

    /**
     * 브랜치, 채널 매칭 목록
     */
    @Tag(name = "브랜치 API")
    @Operation(summary = "브랜치, 채널 매칭 목록 조회", description = "브랜치, 채널 매칭 목록 조회")
    @GetMapping(value = "/with-owned-channel")
    public ResponseEntity<ApiResult<List<BranchChannelDto>>> getAllBranchChannel(
            @Parameter(description = "사용여부", in = ParameterIn.QUERY)
            @RequestParam(value = "enabled", required = false, defaultValue = "true") Boolean enabled,
            @SortDefault(sort = {"name"}, direction = Sort.Direction.ASC) Sort sort) {

        log.info("BRANCH, CHANNEL, GET ALL");

        List<BranchChannelDto> branchChannels = branchService.getAllBranchChannel(enabled, sort);

        ApiResult<List<BranchChannelDto>> response = ApiResult.<List<BranchChannelDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(branchChannels)
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    /**
     * 브랜치 역할 추가 / 삭제
     * @param dtos
     * @return
     */
    @Tag(name = "브랜치 API")
    @Operation(summary = "브랜치 역할 추가 / 삭제", description = "브랜치 역할 추가 및 삭제")
    @PostMapping(value = "/role")
    public ResponseEntity<ApiResult<List<BranchDtoWithRole>>> role (
            @RequestBody @Valid List<BranchRoleDto> dtos) {

        List<BranchDtoWithRole> roles = branchService.rolesDeleteSaveAll(dtos);
        ApiResult<List<BranchDtoWithRole>> response = ApiResult.<List<BranchDtoWithRole>>builder()
                .code(ApiResultCode.succeed)
                .payload(roles)
                .build();

        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }

    /**
     * 브랜치 근무조건 설정
     * @param branchId
     * @param officeWorkDto
     * @return
     */
    @Tag(name = "브랜치 API")
    @Operation(summary = "브랜치 근무조건 설정", description = "브랜치 근무 조건 설정")
    @PutMapping(value = "/{id}/office-housrs")
    public ResponseEntity<ApiResult<BranchDto>> officeHousrs(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long branchId
            , @RequestBody @Valid OfficeWorkDto officeWorkDto) {

        log.info("BRANCH ID :{} , OFFICE WORK DTO:{}" , branchId , officeWorkDto);


        officeWorkDto.setCases(WorkType.Cases.branch);

        BranchDto dto = branchService.getById(branchId);
        if(dto == null){
            return new ResponseEntity<>( null , HttpStatus.BAD_REQUEST);
        }

        dto.setAssign(officeWorkDto.getBranch().getAssign());
        dto.setMaxCounsel(officeWorkDto.getBranch().getMaxCounsel());
        dto.setMaxCounselType(officeWorkDto.getBranch().getMaxCounselType());
        dto.setOffDutyHours(officeWorkDto.getBranch().getOffDutyHours());

        //브랜치 설정 update
        branchService.store(dto);

        log.info("OFFICE HOURS :{}" ,officeWorkDto.getOfficeHours().getDayOfWeek());

        //브랜치 근무 설정 create
        OfficeHoursDto officeHoursDto = officeHoursService.branch(officeWorkDto,branchId);

        officeWorkDto.setBranch(dto);
        officeWorkDto.setOfficeHours(officeHoursDto);
        officeWorkDto.setCases(null);

        log.info("OFFICE WORK :{}" , officeWorkDto);
        dto.setOfficeHours(officeHoursDto);
        ApiResult<BranchDto> response = ApiResult.<BranchDto>builder()
                .code(ApiResultCode.succeed)
                .payload(dto)
                .build();

        return new ResponseEntity<>(response , HttpStatus.ACCEPTED);

    }

    /**
     * 브랜치 근무조건 설정
     * @param branchId
     * @return
     */
    @Tag(name = "브랜치 API")
    @Operation(summary = "브랜치 근무조건 설정", description = "브랜치 근무조건 설정")
    @GetMapping(value = "/{id}/office-housrs")
    public ResponseEntity<ApiResult<BranchDto>> officeHousrs(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long branchId) {

        BranchDto dto = branchService.getById(branchId);
        if(dto == null){
            return new ResponseEntity<>( null , HttpStatus.BAD_REQUEST);
        }

        dto.setOfficeHours(officeHoursService.branch(branchId));
        ApiResult<BranchDto> response = ApiResult.<BranchDto>builder()
                .code(ApiResultCode.succeed)
                .payload(dto)
                .build();

        return new ResponseEntity<>(response , HttpStatus.OK);

    }


    /**
     * 상담 환경설정
     * @param branchId
     * @param dto
     * @return
     */
    @Tag(name = "브랜치 API")
    @Operation(summary = "상담 환경설정", description = "상담 환경설정")
    @PutMapping("/{id}/counsel")
    public ResponseEntity<ApiResult<CounselEnvDto>> counsel(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long branchId
            , @RequestBody @Valid CounselEnvDto dto) {

        dto.setBranchId(branchId);
        CounselEnvDto counselEnvDto = counselEnvService.store(dto);

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiResultCode apiResultCode = ApiResultCode.failed;

        if(counselEnvDto != null){
            httpStatus = HttpStatus.ACCEPTED;
            apiResultCode = ApiResultCode.succeed;
        }

        ApiResult<CounselEnvDto> response = ApiResult.<CounselEnvDto>builder()
                .code(apiResultCode)
                .payload(counselEnvDto)
                .build();

        return new ResponseEntity<>(response , httpStatus);
    }


    /**
     * 상담 환경설정 조회
     * @param branchId
     * @return
     */
    @Tag(name = "브랜치 API")
    @Operation(summary = "상담 환경설정 조회", description = "상담 환경설정 조회")
    @GetMapping("/{id}/counsel")
    public ResponseEntity<ApiResult<CounselEnvDto>> counselGet(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "id") @NotNull Long branchId){

        CounselEnvDto counselEnvDto = counselEnvService.get(branchId);

        ApiResult<CounselEnvDto> response = ApiResult.<CounselEnvDto>builder()
                .code(ApiResultCode.succeed)
                .payload(counselEnvDto)
                .build();

        return new ResponseEntity<>(response , HttpStatus.OK);
    }


    /**
     * 상담 유입경로 생성
     * @param branchId
     * @param dto
     * @return
     */
    @Tag(name = "브랜치 API")
    @Operation(summary = "상담 유입경로 생성", description = "상담 유입경로 생성")
    @PostMapping("/{id}/counsel/inflow")
    public ResponseEntity<ApiResult<CounselInflowEnvDto>> inflowCreate(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long branchId
            , @RequestBody @Valid CounselInflowEnvDto dto) {

        dto.setBranchId(branchId);
        CounselInflowEnvDto counselInflowEnvDto = null;
        try {
            counselInflowEnvDto = counselEnvService.store(dto);
        } catch (DataIntegrityViolationException e) {
            if(e.getLocalizedMessage().contains("UK_COUNSEL_INFLOW_ENV__BRANCH_PARAMS")){
                Map<String, Object> extra = new HashMap<>();
                extra.put("branch_id",dto.getBranchId());
                extra.put("params",dto.getParams());
                throw new BizException(systemMessageProperty.getValidation().getDuplication().getConsultationFunnel(), "SB-SA-005-001" , extra);
            } else {
                throw new BizException();
            }
        }


        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiResultCode apiResultCode = ApiResultCode.failed;

        if(counselInflowEnvDto != null){
            httpStatus = HttpStatus.CREATED;
            apiResultCode = ApiResultCode.succeed;
        }

        ApiResult<CounselInflowEnvDto> response = ApiResult.<CounselInflowEnvDto>builder()
                .code(apiResultCode)
                .payload(counselInflowEnvDto)
                .build();

        return new ResponseEntity<>(response , httpStatus);
    }

    /**
     * 상담 유입경로 수정
     * @param branchId
     * @param dto
     * @return
     */
    @Tag(name = "브랜치 API")
    @Operation(summary = "상담 유입경로 수정", description = "상담 유입경로 수정")
    @PutMapping("/{id}/counsel/inflow")
    public ResponseEntity<ApiResult<CounselInflowEnvDto>> inflowUpdate(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long branchId
            , @RequestBody CounselInflowEnvDto dto) {

        dto.setBranchId(branchId);
        CounselInflowEnvDto counselInflowEnvDto = counselEnvService.store(dto);

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiResultCode apiResultCode = ApiResultCode.failed;

        if(counselInflowEnvDto != null){
            httpStatus = HttpStatus.ACCEPTED;
            apiResultCode = ApiResultCode.succeed;
        }

        ApiResult<CounselInflowEnvDto> response = ApiResult.<CounselInflowEnvDto>builder()
                .code(apiResultCode)
                .payload(counselInflowEnvDto)
                .build();

        return new ResponseEntity<>(response , httpStatus);
    }


    /**
     * 상담 유입경로 삭제
     * @param branchId
     * @param id
     * @return
     */
    @Tag(name = "브랜치 API")
    @Operation(summary = "상담 유입경로 삭제", description = "상담 유입경로 삭제")
    @DeleteMapping("/{id}/counsel/inflow")
    public ResponseEntity<ApiResult<String>> inflowRemove(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long branchId
            ,@Parameter(description = "상담 유입경로 아이디", in = ParameterIn.QUERY, required = true)
            @RequestParam(name = "id") @NotNull Long id) {

        boolean result = counselEnvService.remove(branchId , id);

        ApiResultCode apiResultCode = ApiResultCode.failed;

        if(result){
            apiResultCode = ApiResultCode.succeed;
        }

        ApiResult<String> response = ApiResult.<String>builder()
                .code(apiResultCode)
                .build();

        return new ResponseEntity<>(response , HttpStatus.ACCEPTED);
    }


    /**
     * 상담 유입경로 목록
     * @param branchId
     * @return
     */
    @Tag(name = "브랜치 API")
    @Operation(summary = "상담 유입경로 목록 조회", description = "상담 유입 경로 목록 조회")
    @GetMapping("/{id}/counsel/inflow")
    public ResponseEntity<ApiResult<List<CounselInflowEnvDto>>> inflows(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long branchId
    ) {
        List<CounselInflowEnvDto> counselInflowEnvs = counselEnvService.findAllAndEnabled(branchId);
        ApiResult<List<CounselInflowEnvDto>> response = ApiResult.<List<CounselInflowEnvDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(counselInflowEnvs)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    /**
//     * @param branchDtoList
//     * @return
//     */
//    @Deprecated
//    @PostMapping(value = "/saveAll")
//    @Tag(name = "브랜치 다건 저장 API")
//    @Operation(summary = "브랜치 다건 저장", description = "브랜치 생성")
//    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
//    public ResponseEntity<ApiResult<BranchDto>> saveAll(@RequestBody List<BranchDto> branchDtoList) {
//        try {
//            ApiResult<BranchDto> response = null;
//            for( BranchDto branchDto : branchDtoList ){
//                BranchDto branch =  branchService.store(branchDto);
//                response = ApiResult.<BranchDto>builder().code(ApiResultCode.succeed)
//                        .payload(branch)
//                        .build();
//            }
//            return new ResponseEntity<>(response , HttpStatus.CREATED);
//        } catch (DataIntegrityViolationException e) {
//            log.info("BRANCH DUPLICATION ERROR :  ", e );
//            throw new BizException(systemMessageProperty.getValidation().getDuplication().getBranch());
//        } catch (Exception exception){
//            log.info("BRANCH Create Exception :  ", exception );
//            throw new BizException(exception.getMessage());
//        }
//    }

    /** V2 Apis **/
    @Operation(description = "브랜치 전체 저장")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = PostBranchResponseDto.class)))
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    public ResponseEntity<? super PostBranchResponseDto> postAllBranch(@RequestBody PostBranchRequestDto requestBody) {
        log.info("Post All Branch, Request Body : {}", requestBody);
        ResponseEntity<? super PostBranchResponseDto> response = branchServiceAggregation.postBranch(requestBody);
        log.info("Post All Branch, Response : {}", response);
        return response;
    }

    @Operation(description = "브랜치 수정 V2")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = PatchBranchResponseDto.class)))
    @PatchMapping
    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    public ResponseEntity<? super PatchBranchResponseDto> patchBranch(@RequestBody PatchBranchRequestDto requestBody) {
        log.info("Patch Branch, Request Body : {}", requestBody);
        ResponseEntity<? super PatchBranchResponseDto> response = branchManageService.patchBranch(requestBody);
        log.info("Patch Branch, Response : {}", response);
        return response;
    }
}

