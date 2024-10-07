package com.kep.portal.controller.work;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.work.OfficeHoursDto;
import com.kep.core.model.dto.work.OfficeWorkDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchMapper;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.work.OfficeHoursService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 근무 시간 설정
 */
@Tag(name = "근무시간 설정 API", description = "/api/v1/work")
@Slf4j
@RestController
@RequestMapping("/api/v1/work")
public class OfficeHoursController {

    @Resource
    private OfficeHoursService officeHoursService;

    @Resource
    private BranchRepository branchRepository;

    @Resource
    private BranchMapper branchMapper;

    @Resource
    private BranchService branchService;

    @Resource
    private MemberService memberService;

    /**
     * 브랜치 근무시간 설정
     * @param branchId
     * @param officeWorkDto
     * @return
     */
    @Tag(name = "근무시간 설정 API")
    @Operation(summary = "브랜치 근무시간 설정")
    @PostMapping(value = "/branch/{id}")
    public ResponseEntity<ApiResult<OfficeWorkDto>> branch(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long branchId
            , @RequestBody @Valid OfficeWorkDto officeWorkDto) {
    	log.info("근무 시간 설정 요청: Branch ID - {}, OfficeWorkDto - {}", branchId, officeWorkDto);
        log.info("BRANCH ID :{} , OFFICE WORK DTO:{}" , branchId , officeWorkDto);


        officeWorkDto.setCases(WorkType.Cases.branch);

        Branch branch = branchRepository.findById(branchId).orElse(null);
        if(branch == null){
            return new ResponseEntity<>( null , HttpStatus.BAD_REQUEST);
        }

        BranchDto branchDto = branchMapper.map(branch);
        branchDto.setAssign(officeWorkDto.getBranch().getAssign());
        branchDto.setMaxCounsel(officeWorkDto.getBranch().getMaxCounsel());
        branchDto.setMaxCounselType(officeWorkDto.getBranch().getMaxCounselType());
        branchDto.setOffDutyHours(officeWorkDto.getBranch().getOffDutyHours());

        //브랜치 설정 update
        BranchDto dto = branchService.store(branchDto);

        log.info("OFFICE HOURS :{}" ,officeWorkDto.getOfficeHours().getDayOfWeek());

        //브랜치 근무 설정 create
        OfficeHoursDto officeHoursDto = officeHoursService.branch(officeWorkDto,branchId);
        //OfficeHoursDto officeHoursDto = memberService.saveOfficeHours(officeWorkDto);

        officeWorkDto.setBranch(dto);
        officeWorkDto.setOfficeHours(officeHoursDto);
        officeWorkDto.setCases(null);

        log.info("OFFICE WORK :{}" , officeWorkDto);

        ApiResult<OfficeWorkDto> response = ApiResult.<OfficeWorkDto>builder()
                .code(ApiResultCode.succeed)
                .payload(officeWorkDto)
                .build();

        return new ResponseEntity<>(response , HttpStatus.CREATED);


    }


    /**
     * 브랜치 근무설정 조회
     * @param branchId
     * @return
     */
    @Tag(name = "근무시간 설정 API")
    @Operation(summary = "브랜치 근무설정 조회")
    @GetMapping(value = "/branch/{id}")
    public ResponseEntity<ApiResult<OfficeWorkDto>> get(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long branchId) {

        log.info("BRANCH ID :{}" , branchId);
        try {

            Branch branch = branchRepository.findById(branchId).orElse(null);
            if(branch == null){
                return new ResponseEntity<>( null , HttpStatus.BAD_REQUEST);
            }

            OfficeHoursDto officeHoursDto = officeHoursService.branch(branchId);
            OfficeWorkDto officeWorkDto = OfficeWorkDto.builder()
                    .branch(branchMapper.map(branch))
                    .officeHours(officeHoursDto)
                    .build();

            log.info("OFFICE WORK :{}" , officeWorkDto);

            ApiResult<OfficeWorkDto> response = ApiResult.<OfficeWorkDto>builder()
                    .code(ApiResultCode.succeed)
                    .payload(officeWorkDto)
                    .build();

            return new ResponseEntity<>(response , HttpStatus.OK);
        } catch (Exception e){
            log.error("ERROR MESSAGE : {}", e.getMessage() , e);
            return new ResponseEntity<>(null , HttpStatus.ACCEPTED);
        }

    }
}
