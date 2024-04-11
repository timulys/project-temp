package com.kep.portal.controller.work;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.work.OffDutyHoursDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.work.OffDutyHours;
import com.kep.portal.service.work.OffDutyHoursService;
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
    @GetMapping("/{cases}/{id}")
    public ResponseEntity<ApiResult<List<OffDutyHoursDto>>> lists(
            @PathVariable(name = "cases") @NotNull WorkType.Cases cases
            , @PathVariable(name = "id") @NotNull Long casesId) throws Exception{

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
    @PostMapping("/{cases}/{id}")
    public ResponseEntity<ApiResult<OffDutyHoursDto>> create(
            @PathVariable(name = "cases") @NotNull WorkType.Cases cases
            , @PathVariable(name = "id") @NotNull Long casesId
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
    @PutMapping("/{cases}/{id}")
    public ResponseEntity<ApiResult<OffDutyHoursDto>> update(
            @PathVariable(name = "cases") @NotNull WorkType.Cases cases
            , @PathVariable(name = "id") @NotNull Long casesId
            , @RequestBody @Valid OffDutyHoursDto dto) throws Exception{

        dto.setCases(cases);
        OffDutyHoursDto offDutyHoursDto = offDutyHoursService.create(dto , casesId);
        ApiResult<OffDutyHoursDto> response = ApiResult.<OffDutyHoursDto>builder()
                .code(ApiResultCode.succeed)
                .payload(offDutyHoursDto)
                .build();

        return new ResponseEntity<>(response , HttpStatus.CREATED);

    }


    @DeleteMapping("/{cases}/{id}")
    public ResponseEntity<ApiResult<OffDutyHours>> delete(
            @PathVariable(name = "cases") @NotNull WorkType.Cases cases
            , @PathVariable(name = "id") @NotNull Long casesId
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
