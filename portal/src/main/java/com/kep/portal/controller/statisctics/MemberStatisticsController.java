package com.kep.portal.controller.statisctics;


import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.dto.issue.IssueMemberStatisticsDto;
import com.kep.portal.service.issue.IssueStatisticsService;
import com.kep.portal.util.ZonedDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/statistic")
public class MemberStatisticsController {

    @Resource
    private IssueStatisticsService issueStatisticsService;

    @GetMapping(value = "/branch/{id}/team")
    public ResponseEntity<ApiResult<List<IssueMemberStatisticsDto>>> teams(@PathVariable("id") Long branchId
            , @RequestParam(name = "from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from
            , @RequestParam(name = "to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to
            , @RequestParam(name = "team_id",required = false) Long teamId){


        ZonedDateTime start = ZonedDateTimeUtil.start(from.toString());
        ZonedDateTime end = ZonedDateTimeUtil.end(to.toString());
        List<IssueMemberStatisticsDto> issueMemberStatistics = issueStatisticsService.issueMemberStatistics(start , end , branchId , teamId);
        return new ResponseEntity<>(ApiResult.<List<IssueMemberStatisticsDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(issueMemberStatistics)
                .build(), HttpStatus.OK);
    }

}
