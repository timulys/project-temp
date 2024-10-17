package com.kep.portal.controller.work;


import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.work.MemberMaxCounselDto;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.work.WorkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Tag(name = "최대상담건수 설정 API", description = "/api/v1/work")
@Slf4j
@RestController
@RequestMapping("/api/v1/work")
public class WorkController {

    @Resource
    private WorkService workService;

    @Resource
    private BranchService branchService;

    /**
     * 최대상담건수 개별설정
     * @param branchId
     * @param dto
     * @return
     * @throws Exception
     */
    @Tag(name = "최대상담건수 설정 API")
    @Operation(description = "최대상담건수 개별 설정 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<MemberMaxCounselDto>> put(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long branchId
            , @RequestBody @Validated MemberMaxCounselDto dto, @SortDefault.SortDefaults({
            @SortDefault(sort = {"id"}, direction = Sort.Direction.ASC)}) Pageable pageable) {

        branchService.maxMemberCounsel(branchId , dto);
        log.info("MEMBER MAX COUNSEL:{}",dto.getMemberCounsels());


        dto.setBranchId(branchId);
        dto = workService.memberCounsel(dto , pageable);
        ApiResultCode code = ApiResultCode.failed;
        if(!ObjectUtils.isEmpty(dto)){
            code = ApiResultCode.succeed;
        }

        ApiResult<MemberMaxCounselDto>response = ApiResult.<MemberMaxCounselDto>builder()
                .code(code)
                .payload(dto)
                .build();

        return new ResponseEntity<>(response , HttpStatus.CREATED);

    }


    /**
     * 최대상담건수 개별설정 조회
     * @param branchId
     * @return
     * @throws Exception
     */
    @Tag(name = "최대상담건수 설정 API")
    @Operation(description = "최대상담건수 개별설정 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<MemberMaxCounselDto>> get(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long branchId,
            @SortDefault.SortDefaults({
                    @SortDefault(sort = {"id"}, direction = Sort.Direction.ASC)}) Pageable pageable) {


        MemberMaxCounselDto dto = workService.memberMaxCounsel(branchId , pageable);

        ApiResultCode code = ApiResultCode.failed;
        if(!ObjectUtils.isEmpty(dto)){
            code = ApiResultCode.succeed;
        }

        ApiResult<MemberMaxCounselDto>response = ApiResult.<MemberMaxCounselDto>builder()
                .code(code)
                .payload(dto)
                .build();

        return new ResponseEntity<>(response , HttpStatus.OK);

    }
}
