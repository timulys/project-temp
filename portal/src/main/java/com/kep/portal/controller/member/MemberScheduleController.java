/**
 * MemberSchedule Controller
 *
 *  @생성일자      / 만든사람		 	/  수정내용
 * 	2023.05.17 / asher.shin    /  신규
 */
package com.kep.portal.controller.member;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.dto.member.MemberAutoMessageTemplateDto;
import com.kep.portal.model.dto.member.MemberScheduleDto;
import com.kep.portal.model.entity.member.ScheduleType;
import com.kep.portal.service.member.MemberScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Tag(name = "사용자(계정) 스케줄 API", description = "/api/v1/schedule")
@Slf4j
@RestController
@RequestMapping("/api/v1/schedule")
public class MemberScheduleController {

    @Resource
    private MemberScheduleService memberScheduleService;

    /**
     *
     * FIXME :: 월별 기준 조회인데 yyyy-MM-dd 형식. 수정 필요 20240715 volka
     *
     * 일정 가져오기
     * [2023.06.07] /asher.shin /scheduleType 추가
     */
    @Tag(name = "사용자(계정) 스케줄 API")
    @Operation(summary = "[월기준] 사용자 일정 조회")
    @GetMapping
    public ResponseEntity<ApiResult<List<MemberScheduleDto>>> index(
            @Parameter(description = "사용자 아이디", required = true)
            @RequestParam(value = "member_id") Long memberId
            ,@Parameter(description = "조회일 (yyyy-MM-dd)")
            @RequestParam(value ="date",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        log.info("APPOINTMENT, GET ALL, PARAM: {},{},{}", memberId,date);

        List<MemberScheduleDto> MemberScheduleDtoList = memberScheduleService.getMonth(memberId,date);

        return new ResponseEntity<>(ApiResult.<List<MemberScheduleDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(MemberScheduleDtoList)
                .build(),HttpStatus.OK);
    }



    @Tag(name = "사용자(계정) 스케줄 API")
    @Operation(summary = "[일기준] 사용자 일정 조회")
    @GetMapping(value = "/day")
    public ResponseEntity<ApiResult<List<MemberScheduleDto>>> day(
            @Parameter(description = "사용자 아이디", required = true)
            @RequestParam(value = "member_id") Long memberId
            ,@Parameter(description = "조회일 (yyyy-MM-dd)", required = true)
            @RequestParam(value ="date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        log.info("APPOINTMENT, GET ALL, PARAM: {},{},{}", memberId,date);

        List<MemberScheduleDto> MemberScheduleDtoList = memberScheduleService.getDay(memberId,date);

        return new ResponseEntity<>(ApiResult.<List<MemberScheduleDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(MemberScheduleDtoList)
                .build(),HttpStatus.OK);
    }


    @Tag(name = "사용자(계정) 스케줄 API")
    @Operation(summary = "[주기준] 사용자 일정 조회")
    @GetMapping(value = "/week")
    public ResponseEntity<ApiResult<List<MemberScheduleDto>>> week(
            @Parameter(description = "사용자 아이디", required = true)
            @RequestParam(value = "member_id") Long memberId
            ,@Parameter(description = "조회일 (yyyy-MM-dd)", required = true)
            @RequestParam(value ="date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        log.info("APPOINTMENT, GET ALL, PARAM: {},{},{}", memberId,date);

        List<MemberScheduleDto> MemberScheduleDtoList = memberScheduleService.getWeek(memberId,date);

        return new ResponseEntity<>(ApiResult.<List<MemberScheduleDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(MemberScheduleDtoList)
                .build(),HttpStatus.OK);
    }


    @Tag(name = "사용자(계정) 스케줄 API")
    @Operation(summary = "일정 검색")
    @GetMapping(value = "/search")
    public ResponseEntity<ApiResult<List<MemberScheduleDto>>> search(
            @Parameter(description = "사용자 아이디", required = true)
            @RequestParam(value = "member_id") Long memberId
            ,@Parameter(description = "검색 시작일 (yyyy-MM-dd)", required = true)
            @RequestParam(value ="start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start
            ,@Parameter(description = "검색 종료일 (yyyy-MM-dd)", required = true)
            @RequestParam(value ="end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
            ,@Parameter(description = "스케줄 타입 (holiday, meeting, telephone, etc)")
            @RequestParam(value ="type" , required = false) Set<ScheduleType> scheduleType
            ,@Parameter(description = "완료여부")
            @RequestParam(value ="completed",required = false) Boolean completed
            ,@Parameter(description = "고객명")
            @RequestParam(value ="customer_name",required = false) String name) {

        List<MemberScheduleDto> MemberScheduleDtoList = memberScheduleService.search(memberId,start , end , scheduleType,completed , name);
        return new ResponseEntity<>(ApiResult.<List<MemberScheduleDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(MemberScheduleDtoList)
                .build(),HttpStatus.OK);
    }

    /**
     * 일정 단일 가져오기
     *
     */
    @Tag(name = "사용자(계정) 스케줄 API")
    @Operation(summary = "일정 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<MemberScheduleDto>> show(
            @Parameter(description = "사용자 일정 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(value="id") @Positive Long id) {

        log.info("APPOINTMENT, GET ONE, PARAM: {}", id);

        MemberScheduleDto memberScheduleDto = memberScheduleService.show(id);

        return new ResponseEntity<>(ApiResult.<MemberScheduleDto>builder()
                .code(ApiResultCode.succeed)
                .payload(memberScheduleDto)
                .build(),HttpStatus.OK);
    }


    /**
     * 일정 저장
     */
    @Tag(name = "사용자(계정) 스케줄 API")
    @Operation(summary = "일정 저장")
    @PostMapping
    public ResponseEntity<ApiResult<MemberScheduleDto>> store(
            @RequestBody MemberScheduleDto dto) {

        log.info("APPOINTMENT, POST SAVE, PARAM: {}", dto);

        MemberScheduleDto MemberScheduleDto = memberScheduleService.store(dto);

        return new ResponseEntity<>(ApiResult.<MemberScheduleDto>builder()
                .code(ApiResultCode.succeed)
                .payload(MemberScheduleDto)
                .build(),HttpStatus.CREATED);
    }


    /**
     * 일정 수정
     */
    @Tag(name = "사용자(계정) 스케줄 API")
    @Operation(summary = "일정 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<MemberScheduleDto>> update(
            @Parameter(description = "사용자 스케줄 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(value="id") @Positive Long id,
            @RequestBody MemberScheduleDto dto) {

        dto.setId(id);
        log.info("APPOINTMENT, POST SAVE, PARAM: {}", dto);

        MemberScheduleDto MemberScheduleDto = memberScheduleService.update(dto);

        return new ResponseEntity<>(ApiResult.<MemberScheduleDto>builder()
                .code(ApiResultCode.succeed)
                .payload(MemberScheduleDto)
                .build(),HttpStatus.CREATED);
    }

    /**
     * 일정 삭제
     */
    @Tag(name = "사용자(계정) 스케줄 API")
    @Operation(summary = "일정 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<String>> destroy(
            @Parameter(description = "사용자 스케줄 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(value="id") @Positive Long id) {
        boolean result = memberScheduleService.delete(id);
        ApiResultCode apiResultCode = result ? ApiResultCode.succeed : ApiResultCode.failed;
        ApiResult<String> response = ApiResult.<String>builder()
                .code(apiResultCode)
                .build();
        return new ResponseEntity<>(response , HttpStatus.ACCEPTED);
    }

    /**
     * 일정 완료
     * @param id
     * @return
     */
    @Tag(name = "사용자(계정) 스케줄 API")
    @Operation(summary = "일정 완료")
    @PostMapping("/completed/{id}")
    public ResponseEntity<ApiResult<MemberScheduleDto>> completed(
            @Parameter(description = "사용자 스케줄 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(value="id") @Positive Long id) {

        MemberScheduleDto MemberScheduleDto = memberScheduleService.completed(id);

        return new ResponseEntity<>(ApiResult.<MemberScheduleDto>builder()
                .code(ApiResultCode.succeed)
                .payload(MemberScheduleDto)
                .build(),HttpStatus.CREATED);
    }

    @Tag(name = "사용자(계정) 스케줄 API")
    @Operation(summary = "템플릿 조회")
    @GetMapping("/template")
    public ResponseEntity<ApiResult<List<MemberAutoMessageTemplateDto>>> template(
            @RequestParam(value = "category_code",required = false) Integer category
    ) {

        List<MemberAutoMessageTemplateDto> memberAutoMessageTemplates = memberScheduleService.getCategoryTemplate(category);

        return new ResponseEntity<>(ApiResult.<List<MemberAutoMessageTemplateDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(memberAutoMessageTemplates)
                .build(),HttpStatus.OK);
    }
}
