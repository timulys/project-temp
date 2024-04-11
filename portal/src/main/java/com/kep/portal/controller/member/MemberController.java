package com.kep.portal.controller.member;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.member.ProfileDto;
import com.kep.core.model.type.QueryParam;
import com.kep.portal.model.dto.member.MemberAssignDto;
import com.kep.portal.model.dto.member.MemberPassDto;
import com.kep.portal.model.dto.member.MemberSearchCondition;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.service.member.MemberService;

import lombok.extern.slf4j.Slf4j;

/**
 * 유저
 * <li>시스템 설정 > 계정 관리 > 계정 목록, SB-SA-002</li>
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    /**
     * 브랜치별 목록 조회
     */
    @GetMapping(value = "/branch/{id}")
    public ResponseEntity<ApiResult<List<MemberDto>>> getAllByBranch(@NotNull @PathVariable("id") Long branchId) {

        log.info("MEMBER, GET ALL BY BRANCH, BRANCH: {}", branchId);

        List<MemberDto> items = memberService.findByBranchId(branchId);
        return new ResponseEntity<>(ApiResult.<List<MemberDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(items)
                .build(), HttpStatus.OK);
    }


    /**
     * TODO: 용도?
     * {@link #getAllByBranch} 과 같음
     */
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
    @GetMapping
    public ResponseEntity<ApiResult<List<MemberDto>>> getAll(
            @QueryParam @Valid MemberSearchCondition condition,
            @SortDefault.SortDefaults({
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
     * 조회
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResult<MemberDto>> get(
            @PathVariable("id") Long id) {

        log.info("MEMBER, GET, MEMBER: {}", id);

        MemberDto memberDto = memberService.get(id);

        if (memberDto == null) {
            return new ResponseEntity<>(new ApiResult<>(ApiResultCode.failed), HttpStatus.NOT_FOUND);
        } else {
            ApiResult<MemberDto> response = ApiResult.<MemberDto>builder()
                    .code(ApiResultCode.succeed)
                    .payload(memberDto)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    /**
     * 검색
     */
    @GetMapping(value = "/search")
//    @PreAuthorize("hasAnyAuthority('READ_MEMBER')")
    public ResponseEntity<ApiResult<List<MemberDto>>> search(
            @QueryParam @Valid MemberSearchCondition condition,
            @PageableDefault(size = 1000, sort = {"nickname"}, direction = Sort.Direction.ASC) Pageable pageable) {

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
//    @PreAuthorize("hasAnyAuthority('READ_MEMBER')")
    public ResponseEntity<ApiResult<List<MemberAssignDto>>> searchAssignable(
            @QueryParam @Valid MemberSearchCondition condition,
            @PageableDefault(size = 1000, sort = {"nickname"}, direction = Sort.Direction.ASC) Pageable pageable) {

        log.info("MEMBER, SEARCH ASSIGNABLE, PARAM: {}", condition);

        Page<MemberAssignDto> page = memberService.searchAssignable(condition, pageable);

        ApiResult<List<MemberAssignDto>> response = ApiResult.<List<MemberAssignDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(page.getContent())
                .currentPage(page.getNumber())
                .totalPage(page.getTotalPages())
                .totalElement(page.getTotalElements())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 생성
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('WRITE_ACCOUNT') or hasAnyRole('ROLE_MASTER')")
    public ResponseEntity<ApiResult<MemberDto>> post(
            @RequestBody @Valid MemberDto dto)  {

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
    @PutMapping(value = "/{id}")
    public ResponseEntity<ApiResult<MemberDto>> put(
            @PathVariable("id") Long memberId,
            @RequestBody MemberDto dto) {

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
    @PutMapping(value = "/{id}/password")
    public ResponseEntity<ApiResult<String>> put(
            @PathVariable("id") Long memberId) {

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
    @GetMapping(value = "/check")
    public ResponseEntity<ApiResult<String>> duplication(
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
     */
    @GetMapping(value = "/checkVndrCustNo")
    public ResponseEntity<ApiResult<String>> duplicationVndrCustNo(
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
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResult<String>> status(
            @PathVariable("id") Long memberId,
            @RequestBody ProfileDto dto){

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
    @PutMapping(value = "/{id}/change")
    public ResponseEntity<ApiResult<Map<String,Object>>> change(
            @PathVariable("id") Long memberId ,@RequestBody MemberPassDto dto){

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
     * @param Long id (Pathvariable)
     * @return ResponseEntity
     * @throws
     *
     * @수정일자	  / 수정자		 	/ 수정내용
     * 2023.05.31 / asher.shin / 함수추가
     */
    @PutMapping(value = "/{id}/duty/change")
    public ResponseEntity<ApiResult<MemberDto>> changeDuty(
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
     * @param Long id (Pathvariable)
     * @return ResponseEntity
     * @throws
     *
     * @수정일자	  / 수정자		 	/ 수정내용
     * 2023.05.31 / asher.shin / 함수추가
     */
    @PutMapping(value = "/{id}/counsel/change")
    public ResponseEntity<ApiResult<MemberDto>> changeCounselCategory(
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
    @GetMapping("/not-in")
    public ResponseEntity<ApiResult<List<MemberDto>>> getNotLeaders(
            @QueryParam @Valid MemberSearchCondition condition,
            @SortDefault.SortDefaults({
                    @SortDefault(sort = {"id"}, direction = Sort.Direction.DESC)}) Pageable pageable) {

        log.info("MEMBER, GET ALL, PARAM: {}", condition);

        Page<MemberDto> page = memberService.notIn(condition, pageable);
        return response(page, HttpStatus.OK);
    }
}
