package com.kep.portal.controller.statisctics;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.dto.platform.BizTalkHistoryStatisticsDto;
import com.kep.portal.service.platform.BizTalkHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "비즈톡 통계 API", description = "/api/v1/statistic/biztalk")
@Slf4j
@RestController
@RequestMapping("/api/v1/statistic/biztalk")
public class BizTalkStatisticsController {

    @Resource
    private BizTalkHistoryService bizTalkHistoryService;

    @Tag(name = "비즈톡 통계 API")
    @Operation(summary = "발송 이력 조회")
    @GetMapping(value = "/history")
    public ResponseEntity<ApiResult<BizTalkHistoryStatisticsDto>> history(

            @Parameter(description = "조회 시작일 (yyyy-MM-dd)", required = true)
            @RequestParam(name = "from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from
            ,@Parameter(description = "조회 종료일 (yyyy-MM-dd)", required = true)
            @RequestParam(name = "to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to
            ,@Parameter(description = "브랜치 아이디")
            @RequestParam(name = "branch_id",required = false) Long branchId
            ,@Parameter(description = "팀 아이디")
            @RequestParam(name = "team_id",required = false) Long teamId
            ,@Parameter(description = "사용자 아이디")
            @RequestParam(name = "member_id",required = false) Long memberId
            ,@Parameter(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template, legacy_web, legacy_app , kakao_counsel_center)")
            @RequestParam(name = "type" , required = false) PlatformType type)
    {
        BizTalkHistoryStatisticsDto dto = bizTalkHistoryService.statistics(from , to , branchId , teamId , memberId , type);
        ApiResult<BizTalkHistoryStatisticsDto> response = ApiResult.<BizTalkHistoryStatisticsDto>builder()
                .code(ApiResultCode.succeed)
                .payload(dto)
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @Tag(name = "비즈톡 통계 API")
    @Operation(summary = "발송이력 통계 조회")
    @GetMapping(value = "/history/sum")
    public ResponseEntity<ApiResult<BizTalkHistoryStatisticsDto>> historySum(
            @Parameter(description = "조회 시작일 (yyyy-MM-dd)", required = true)
            @RequestParam(name = "from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from
            ,@Parameter(description = "조회 종료일 (yyyy-MM-dd)", required = true)
            @RequestParam(name = "to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to
            ,@Parameter(description = "브랜치 아이디")
            @RequestParam(name = "branch_id",required = false) Long branchId
            ,@Parameter(description = "팀 아이디")
            @RequestParam(name = "team_id",required = false) Long teamId
            ,@Parameter(description = "사용자 아이디")
            @RequestParam(name = "member_id",required = false) Long memberId
            ,@Parameter(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template, legacy_web, legacy_app , kakao_counsel_center)")
            @RequestParam(name = "type" , required = false) PlatformType type)
    {
        BizTalkHistoryStatisticsDto dto = bizTalkHistoryService.sum(from , to , branchId , teamId , memberId , type);
        ApiResult<BizTalkHistoryStatisticsDto> response = ApiResult.<BizTalkHistoryStatisticsDto>builder()
                .code(ApiResultCode.succeed)
                .payload(dto)
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

}
