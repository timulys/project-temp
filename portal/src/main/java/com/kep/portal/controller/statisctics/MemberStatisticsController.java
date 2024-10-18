package com.kep.portal.controller.statisctics;


import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.dto.issue.IssueMemberStatisticsDto;
import com.kep.portal.service.issue.IssueStatisticsService;
import com.kep.portal.util.ZonedDateTimeUtil;
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
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Tag(name = "사용자 통계 API", description = "/api/v1/statistic")
@Slf4j
@RestController
@RequestMapping("/api/v1/statistic")
public class MemberStatisticsController {

    @Resource
    private IssueStatisticsService issueStatisticsService;

    @Tag(name = "사용자 통계 API")
    @Operation(summary = "팀 통계 조회")
    @GetMapping(value = "/branch/{id}/team")
    public ResponseEntity<ApiResult<List<IssueMemberStatisticsDto>>> teams(
            @Parameter(description = "브랜치 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable("id") Long branchId
            ,@Parameter(description = "조회 시작일 (yyyy-MM-dd)", required = true)
            @RequestParam(name = "from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from
            ,@Parameter(description = "조회 종료일 (yyyy-MM-dd)", required = true)
            @RequestParam(name = "to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to
            ,@Parameter(description = "팀 아이디")
            @RequestParam(name = "team_id",required = false) Long teamId){

        ZonedDateTime start = ZonedDateTimeUtil.start(from.toString());
        ZonedDateTime end = ZonedDateTimeUtil.end(to.toString());
        List<IssueMemberStatisticsDto> issueMemberStatistics = issueStatisticsService.issueMemberStatistics(start , end , branchId , teamId);
        return new ResponseEntity<>(ApiResult.<List<IssueMemberStatisticsDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(issueMemberStatistics)
                .build(), HttpStatus.OK);
    }

}
