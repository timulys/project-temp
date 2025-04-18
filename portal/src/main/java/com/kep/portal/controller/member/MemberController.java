package com.kep.portal.controller.member;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.member.ProfileDto;
import com.kep.core.model.type.QueryParam;
import com.kep.portal.model.dto.member.MemberAssignDto;
import com.kep.portal.model.dto.member.MemberPassDto;
import com.kep.portal.model.dto.member.MemberSearchCondition;
import com.kep.portal.model.dto.member.response.GetMemberListResponseDto;
import com.kep.portal.model.dto.member.response.GetMemberResponseDto;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.member.MemberServiceV2;
import com.kep.portal.service.member.aggregation.MemberServiceAggregation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 유저
 * <li>시스템 설정 > 계정 관리 > 계정 목록, SB-SA-002</li>
 */
@Tag(name = "사용자(계정) 관리 API", description = "/api/v1/member")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    // Autowired Components
    private final MemberServiceV2 memberServiceV2;
    private final MemberServiceAggregation memberServiceAggregation;
    @Resource
    private MemberService memberService;

    /**
     * 브랜치별 목록 조회
     */
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "브랜치별 목록 조회")
    @GetMapping(value = "/branch/{id}")
    public ResponseEntity<ApiResult<List<MemberDto>>> getAllByBranch(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @NotNull @PathVariable("id") Long branchId
    ) {

        log.info("MEMBER, GET ALL BY BRANCH, BRANCH: {}", branchId);

        List<MemberDto> items = memberService.findByBranchId(branchId);
        return new ResponseEntity<>(ApiResult.<List<MemberDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(items)
                .build(), HttpStatus.OK);
    }


    /**
     * FIXME :: 위 getAllByBranch와 동일 코드. 앞단 연동 확인 후 제거 20240715 volka
     * TODO: 용도?
     * {@link #getAllByBranch} 과 같음
     */
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "브랜치별 목록 조회와 동일함. 앞단 연동 확인 후 제거예정")
    @GetMapping(value = "/branch/{id}/team")
    public ResponseEntity<ApiResult<List<MemberDto>>> teams(@NotNull @PathVariable("id") Long branchId) {

        log.info("MEMBER, GET ALL WITH TEAM, BRANCH: {}", branchId);

        List<MemberDto> items = memberService.findByBranchId(branchId);
        return new ResponseEntity<>(ApiResult.<List<MemberDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(items)
                .build(), HttpStatus.OK);
    }

    /**
     * 계정 관리 > 계정 목록
     */
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "계정 목록 조회", description = "계정 관리 > 계정 목록")
    @GetMapping
    public ResponseEntity<ApiResult<List<MemberDto>>> getAll(
            @ParameterObject @QueryParam @Valid MemberSearchCondition condition,
            @ParameterObject @SortDefault.SortDefaults({
                    @SortDefault(sort = {"id"}, direction = Sort.Direction.DESC)}) Pageable pageable) {

        log.info("MEMBER, GET ALL, PARAM: {}", condition);

        Page<MemberDto> page = memberService.items(condition,pageable);
        return response(page, HttpStatus.OK);
    }

    /**
     * 목록 조회시 응답
     */
    private ResponseEntity<ApiResult<List<MemberDto>>> response(@NotNull Page<MemberDto> page, @NotNull HttpStatus httpStatus) {

        return new ResponseEntity<>(ApiResult.<List<MemberDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(page.getContent())
                .currentPage(page.getNumber())
                .totalPage(page.getTotalPages())
                .totalElement(page.getTotalElements())
                .build(), httpStatus);
    }

    /**
     * 검색
     */
    @GetMapping(value = "/v1/search")
//  @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "사용자 계정 목록 조회")
//    @PreAuthorize("hasAnyAuthority('READ_MEMBER')")
    public ResponseEntity<ApiResult<List<MemberDto>>> search(
            @ParameterObject @QueryParam @Valid MemberSearchCondition condition,
            @ParameterObject @PageableDefault(size = 1000, sort = {"nickname"}, direction = Sort.Direction.ASC) Pageable pageable) {

        log.info("MEMBER, SEARCH, PARAM: {}", condition);

        Page<MemberDto> page = memberService.search(condition, pageable);
        return response(page, HttpStatus.OK);
    }

    /**
     * 배정 가능 상담원 목록
     *
     * <li>상담 포탈 > 상담 직원 전환, SB-CP-P01
     * <li>상담 관리 > 상담 직원 전환, SB-CA-005
     */
    @GetMapping(value = "/search/assignable")
//  @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "배정 가능 상담원 목록 조회", description = "상담 포탈 > 상담 직원 전환, SB-CP-P01\n" +
            "상담 관리 > 상담 직원 전환, SB-CA-005")
//    @PreAuthorize("hasAnyAuthority('READ_MEMBER')")
    public ResponseEntity<ApiResult<List<MemberAssignDto>>> searchAssignable(
            @ParameterObject @QueryParam @Valid MemberSearchCondition condition,
            @ParameterObject @PageableDefault(size = 1000, sort = {"nickname"}, direction = Sort.Direction.ASC) Pageable pageable) {

        log.info("MEMBER, SEARCH ASSIGNABLE, PARAM: {}", condition);

        List<MemberAssignDto> memberAssignDtoList = memberService.searchAssignable(condition, pageable);

        ApiResult<List<MemberAssignDto>> response = ApiResult.<List<MemberAssignDto>>builder().code(ApiResultCode.succeed)
                                                                                              .payload(memberAssignDtoList)
                                                                                              .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 생성
     */
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "사용자 계정 생성")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('WRITE_ACCOUNT') or hasAnyRole('ROLE_MASTER')")
    public ResponseEntity<ApiResult<MemberDto>> post(
            @RequestBody @Valid MemberDto dto) throws Exception {

        log.info("MEMBER, POST, BODY: {}", dto);
        MemberDto result = memberService.store(dto);
        ApiResultCode code = ApiResultCode.failed;
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        if(!ObjectUtils.isEmpty(result)){
            code = ApiResultCode.succeed;
            httpStatus = HttpStatus.CREATED;
        }
        ApiResult<MemberDto> response = ApiResult.<MemberDto>builder()
                .code(code)
                .payload(result)
                .build();

        return new ResponseEntity<>(response , httpStatus);
    }
    
    /**
     * BNK 계정등록 멤버 제한 
     */
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "전체 계정 개수 조회")
    @GetMapping(value = "/totalAccount")
    public ResponseEntity<ApiResult<Long>> getMemberCount() {
        long memberCount = memberService.getTotalmembers();
        ApiResult<Long> response = ApiResult.<Long>builder()
                .code(ApiResultCode.succeed)
                .payload(memberCount)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * 수정
     */
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "사용자 계정 수정")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ApiResult<MemberDto>> put(
            @Parameter(description = "사용자 계정 아이디")
            @PathVariable("id") Long memberId,
            @RequestBody MemberDto dto) throws Exception {

        log.info("MEMBER, PUT, ID: {}, BODY: {}", memberId, dto);
        dto.setId(memberId);
        MemberDto result = memberService.store(dto);
        ApiResultCode code = ApiResultCode.failed;
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        if(!ObjectUtils.isEmpty(result)){
            code = ApiResultCode.succeed;
            httpStatus = HttpStatus.CREATED;
        }
        ApiResult<MemberDto> response = ApiResult.<MemberDto>builder()
                .code(code)
                .payload(result)
                .build();

        return new ResponseEntity<>(response , httpStatus);
    }

    /**
     * 비밀번호 초기화
     */
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "사용자 계정 비밀번호 초기화")
    @PutMapping(value = "/{id}/password")
    public ResponseEntity<ApiResult<String>> put(
            @Parameter(description = "사용자 계정 아이디")
            @PathVariable("id") Long memberId) throws Exception {

        if(memberId == null){
            ApiResult<String> response = ApiResult.<String>builder()
                    .code(ApiResultCode.failed)
                    .build();

            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
        }

        log.info("MEMBER, SET PASSWORD, MEMBER: {}", memberId);

        boolean result = memberService.resetPassword(memberId);

        ApiResult<String> response = ApiResult.<String>builder()
                .code(result ? ApiResultCode.succeed : ApiResultCode.failed)
                .build();

        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }

    /**
     * 계정 username 중복검사
     */
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "사용자 계정 username 중복 체크")
    @GetMapping(value = "/check")
    public ResponseEntity<ApiResult<String>> duplication(
            @Parameter(description = "사용자명", required = true)
            @RequestParam("username") @NotEmpty String username) {

        log.info("MEMBER, CHECK DUPLICATION, USERNAME: {}", username);

        if(!StringUtils.hasLength(username)){
            ApiResult<String> response = ApiResult.<String>builder()
                    .code(ApiResultCode.failed)
                    .build();

            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
        }

        boolean result = memberService.duplicationUserName(username);

        ApiResult<String> response = ApiResult.<String>builder()
                .code(result ? ApiResultCode.succeed : ApiResultCode.failed)
                .build();

        return new ResponseEntity<>(response , HttpStatus.OK);
    }
    /**
     * 계정 vndrCustNo[상담직원번호] 중복검사
     * FIXME :: BNK 로직 20240715 volka
     */
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "계정 상담직원번호 중복검사 (BNK :: 제거 예정)")
    @GetMapping(value = "/checkVndrCustNo")
    public ResponseEntity<ApiResult<String>> duplicationVndrCustNo(
            @Parameter(description = "BNK 상담 직원 번호")
            @RequestParam("vndrCustNo") @NotEmpty String vndrCustNo) {

        log.info("MEMBER, CHECK DUPLICATION, vndrCustNo: {}", vndrCustNo);

        if(!StringUtils.hasLength(vndrCustNo)){
            ApiResult<String> response = ApiResult.<String>builder()
                    .code(ApiResultCode.failed)
                    .build();

            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
        }

        boolean result = memberService.duplicationVndrCustNo(vndrCustNo);

        ApiResult<String> response = ApiResult.<String>builder()
                .code(result ? ApiResultCode.succeed : ApiResultCode.failed)
                .build();

        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    /**
     * 상담가능 상태 ({@link Member}.status) 수정
     */
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "상담가능 상태 수정")
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResult<String>> status (
            @Parameter(description = "사용자(계정) 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable("id") Long memberId,
            @RequestBody ProfileDto dto) throws Exception {

        log.info("MEMBER, SET STATUS, MEMBER: {}, BODY: {}", memberId, dto);

        boolean result = memberService.status(memberId , dto.getStatus());

        ApiResult<String> response = ApiResult.<String>builder()
                .code(result ? ApiResultCode.succeed : ApiResultCode.failed)
                .build();

        return new ResponseEntity<>(response , HttpStatus.CREATED);

    }


    /**
     * 상담직원 환경설정
     * @param setting
     * @return
     */
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "상담직원 환경설정")
    @PutMapping("/setting")
    public ResponseEntity<ApiResult<Map<String , Object>>> setting(
            @RequestBody Map<String , Object> setting){

        log.info("MEMBER SETTING , BODY: {}",setting);
        ApiResult<Map<String,Object>> response = ApiResult.<Map<String,Object>>builder()
                .code(ApiResultCode.succeed)
                .payload(memberService.setting(setting))
                .build();

        return new ResponseEntity<>(response , HttpStatus.CREATED);

    }
    
    
    /**
     * 비밀번호 변경
     * @return ResponseEntity
     * @throws 
     *
     * @수정일자	  / 수정자		 	/ 수정내용
     * 2023.04.04 / philip.lee7 / 함수추가
     */
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "비밀번호 변경")
    @PutMapping(value = "/{id}/change")
    public ResponseEntity<ApiResult<Map<String,Object>>> change(
            @Parameter(description = "사용자 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable("id") Long memberId
            ,@RequestBody MemberPassDto dto) throws Exception {

        if(memberId == null){
            ApiResult<Map<String,Object>> response = ApiResult.<Map<String,Object>>builder()
                    .code(ApiResultCode.failed)
                    .build();

            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
        }

        log.info("MEMBER, CHANGE PASSWORD, MEMBERID : {} ", memberId);
        dto.setId(memberId);
        Map<String,Object> map = memberService.changePassword(dto);

        ApiResult< Map<String,Object>> response = ApiResult.< Map<String,Object>>builder()
                .payload(map)
                .code((boolean)map.get("result") ? ApiResultCode.succeed : ApiResultCode.failed)
                .build();

        return new ResponseEntity<>(response , HttpStatus.ACCEPTED);
    }

    /**
     * 근무시간 일정 예외
     * @param id (Pathvariable)
     * @return ResponseEntity
     * @throws
     *
     * @수정일자	  / 수정자		 	/ 수정내용
     * 2023.05.31 / asher.shin / 함수추가
     */
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "근무시간 일정 수정")
    @PutMapping(value = "/{id}/duty/change")
    public ResponseEntity<ApiResult<MemberDto>> changeDuty(
            @Parameter(description = "사용자 아이디", in = ParameterIn.PATH, required = true)
           @PathVariable(value = "id") Long id ){

        if(id == null){
            ApiResult<MemberDto> response = ApiResult.<MemberDto>builder()
                    .code(ApiResultCode.failed)
                    .build();

            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
        }

        log.info("MEMBER, CHANGE OFF DUTY COUNSEL, ID : {} ", id);
        MemberDto memberDto = memberService.changeOffDuty(id);

        ApiResult<MemberDto> response = ApiResult.<MemberDto>builder()
                .payload(memberDto)
                .code(memberDto!=null ? ApiResultCode.succeed : ApiResultCode.failed)
                .build();

        return new ResponseEntity<>(response , HttpStatus.ACCEPTED);
    }


    /**
     * 상담 카테고리 변경
     * @param id (Pathvariable)
     * @return ResponseEntity
     * @throws
     *
     * @수정일자	  / 수정자		 	/ 수정내용
     * 2023.05.31 / asher.shin / 함수추가
     */
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "상담 카테고리 변경")
    @PutMapping(value = "/{id}/counsel/change")
    public ResponseEntity<ApiResult<MemberDto>> changeCounselCategory(
            @Parameter(description = "사용자 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "id") Long id ,@RequestBody MemberDto dto){

        if(id == null){
            ApiResult<MemberDto> response = ApiResult.<MemberDto>builder()
                    .code(ApiResultCode.failed)
                    .build();

            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
        }

        log.info("MEMBER, CHANGE OFF DUTY COUNSEL, ID ,CATEGORY: {} {} ", id,dto.getCounselCategory());
        MemberDto memberDto = memberService.changeCounselCateogry(id,dto.getCounselCategory());

        ApiResult<MemberDto> response = ApiResult.<MemberDto>builder()
                .payload(memberDto)
                .code(memberDto!=null ? ApiResultCode.succeed : ApiResultCode.failed)
                .build();

        return new ResponseEntity<>(response , HttpStatus.ACCEPTED);
    }

    /**
     * 계정 관리 > 계정 목록 > 관리 > > 그룹 추가 > 그룹장 선택
     */
    @Deprecated
    @Tag(name = "사용자(계정) 관리 API")
    @Operation(summary = "그룹장 선택 조회", description = "계정 관리 > 계정 목록 > 관리 > > 그룹 추가 > 그룹장 선택")
    @GetMapping("/not-in")
    public ResponseEntity<ApiResult<List<MemberDto>>> getNotLeaders(
            @ParameterObject @QueryParam @Valid MemberSearchCondition condition,
            @ParameterObject @SortDefault.SortDefaults({
                    @SortDefault(sort = {"id"}, direction = Sort.Direction.DESC)}) Pageable pageable) {

        log.info("MEMBER, GET ALL, PARAM: {}", condition);

        Page<MemberDto> page = memberService.notIn(condition, pageable);
        return response(page, HttpStatus.OK);
    }

    /** V2 Apis **/
    @Operation(summary = "사용자 계정 상세 조회(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetMemberResponseDto.class)))
    @GetMapping("/{id}")
    public ResponseEntity<? super GetMemberResponseDto> getMember(
             @Parameter(description = "Member 아이디") @PathVariable("id") Long id) {
        log.info("Get Member, Member ID : {}", id);
        ResponseEntity<? super GetMemberResponseDto> response = memberServiceAggregation.getMember(id);
        log.info("Get Member, Response : {}", response);
        return response;
    }

    @Operation(summary = "상담 그룹에 소속된 계정 조회")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetMemberListResponseDto.class)))
    @GetMapping(value = "/search")
    public ResponseEntity<? super GetMemberListResponseDto> getTeamMember(
            @Parameter(description = "상담그룹 ID") @RequestParam("team_id") Long teamId) {
        log.info("Get Member List, Team ID : {}", teamId);
        ResponseEntity<? super GetMemberListResponseDto> response = memberServiceV2.getTeamMember(teamId);
        log.info("Get Member List, Response : {}", response);
        return response;
    }

    @Operation(summary = "상담 그룹 매니저(그룹장) 대상자 조회")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetMemberListResponseDto.class)))
    @GetMapping("/group-manager")
    public ResponseEntity<? super GetMemberListResponseDto> getGroupManagerMember(
            @Parameter(description = "상담원 등급") @RequestParam("level_type") String levelType,
            @Parameter(description = "Branch ID") @RequestParam("branch_id") Long branchId) {
        log.info("Get Group Manager, Level Type : {}, Branch ID : {}", levelType, branchId);
        ResponseEntity<? super GetMemberListResponseDto> response = memberServiceV2.getGroupManagerMember(levelType, branchId);
        log.info("Get Group Manager, Response : {}", response);
        return response;
    }
}
