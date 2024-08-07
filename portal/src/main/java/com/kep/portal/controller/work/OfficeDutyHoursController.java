package com.kep.portal.controller.work;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.work.OffDutyHoursDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.work.OffDutyHours;
import com.kep.portal.service.work.OffDutyHoursService;
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
import java.util.List;

/**
 * 근무 예외 시간 설정
 */
@Tag(name = "근무 예외 시간 설정 API", description = "/api/v1/rest")
@Slf4j
@RestController
@RequestMapping("/api/v1/rest")
public class OfficeDutyHoursController {

    @Resource
    private OffDutyHoursService offDutyHoursService;



    /**
     * 근무 예외 목록
     * @param cases
     * @param casesId
     * @return
     * @throws Exception
     */
    @Tag(name = "근무 예외 시간 설정 API")
    @Operation(summary = "근무 예외 목록 조회")
    @GetMapping("/{cases}/{id}")
    public ResponseEntity<ApiResult<List<OffDutyHoursDto>>> lists(

            @Parameter(description = "근무시간 타입(branch, member)", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "cases") @NotNull WorkType.Cases cases
            ,@Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long casesId) throws Exception{

        List<OffDutyHoursDto> offDutyHoursDtoList = offDutyHoursService.findAll(cases , casesId);

        ApiResult<List<OffDutyHoursDto>> response = ApiResult.<List<OffDutyHoursDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(offDutyHoursDtoList)
                .build();

        return new ResponseEntity<>(response , HttpStatus.CREATED);

    }

    /**
     * 근무 예외 추가
     * @param cases
     * @param casesId
     * @param dto
     * @return
     * @throws Exception
     */
    @Tag(name = "근무 예외 시간 설정 API")
    @Operation(summary = "근무 예외 추가")
    @PostMapping("/{cases}/{id}")
    public ResponseEntity<ApiResult<OffDutyHoursDto>> create(
            @Parameter(description = "근무시간 유형(branch, member)")
            @PathVariable(name = "cases") @NotNull WorkType.Cases cases
            ,@Parameter(description = "(근무시간 유형에 따라 - branchId or memberId)")
            @PathVariable(name = "id") @NotNull Long casesId
            , @RequestBody @Valid OffDutyHoursDto dto) throws Exception{
    	log.info("근무 예외 추가 요청: Cases - {}, Cases ID - {}, DTO - {}", cases, casesId, dto);
        log.info("DTO : {}", dto);
        dto.setCases(cases);

        OffDutyHoursDto offDutyHoursDto = offDutyHoursService.create(dto , casesId);
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ApiResultCode code = ApiResultCode.failed;
        if(offDutyHoursDto != null){
            status = HttpStatus.CREATED;
            code = ApiResultCode.succeed;
        }

        ApiResult<OffDutyHoursDto> response = ApiResult.<OffDutyHoursDto>builder()
                .code(code)
                .payload(offDutyHoursDto)
                .build();

        return new ResponseEntity<>(response ,status);

    }

    /**
     * 근무 예외 수정
     * @param cases
     * @param casesId
     * @param dto
     * @return
     * @throws Exception
     */
    @Tag(name = "근무 예외 시간 설정 API")
    @Operation(summary = "근무 예외 수정")
    @PutMapping("/{cases}/{id}")
    public ResponseEntity<ApiResult<OffDutyHoursDto>> update(
            @Parameter(description = "근무 예외시간 유형 (branch, member)", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "cases") @NotNull WorkType.Cases cases
            ,@Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "id") @NotNull Long casesId
            , @RequestBody @Valid OffDutyHoursDto dto) throws Exception{

        dto.setCases(cases);
        OffDutyHoursDto offDutyHoursDto = offDutyHoursService.create(dto , casesId);
        ApiResult<OffDutyHoursDto> response = ApiResult.<OffDutyHoursDto>builder()
                .code(ApiResultCode.succeed)
                .payload(offDutyHoursDto)
                .build();

        return new ResponseEntity<>(response , HttpStatus.CREATED);

    }


    @Tag(name = "근무 예외 시간 설정 API")
    @Operation(summary = "")
    @DeleteMapping("/{cases}/{id}")
    public ResponseEntity<ApiResult<OffDutyHours>> delete(
            @Parameter(description = "근무 예외시간 유형 (branch, member)", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "cases") @NotNull WorkType.Cases cases
            ,@Parameter(description = "브랜치 아이디 (사용안됨)") //FIXME :: 사용 안함 20240715 volka
            @PathVariable(name = "id") @NotNull Long casesId
            , @RequestBody OffDutyHoursDto dto) throws Exception{

        dto.setCases(cases);
        ApiResultCode code = ApiResultCode.failed;
        boolean result = offDutyHoursService.delete(dto);

        if(result){
            code = ApiResultCode.succeed;
        }

        ApiResult<OffDutyHours> response = ApiResult.<OffDutyHours>builder()
                .code(code)
                .payload(OffDutyHours
                        .builder()
                        .id(dto.getId())
                        .build())
                .build();

        return new ResponseEntity<>(response , HttpStatus.OK);

    }

}
