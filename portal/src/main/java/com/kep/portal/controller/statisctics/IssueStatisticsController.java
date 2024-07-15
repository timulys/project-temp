package com.kep.portal.controller.statisctics;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.branch.BranchChannelDto;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.dto.statistics.IssueStatisticsDto;
import com.kep.portal.service.issue.IssueStatisticsService;
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

@Tag(name = "이슈 통계 API", description = "/api/v1/statistic")
@Slf4j
@RestController
@RequestMapping("/api/v1/statistic")
public class IssueStatisticsController {

    @Resource
    private IssueStatisticsService issueStatisticsService;

    /**
     * group by 형태
     * @param from
     * @param to
     * @param branchId
     * @param teamId
     * @param memberId
     * @return
     */
    @Tag(name = "이슈 통계 API")
    @Operation(summary = "이슈 그룹 조회")
    @GetMapping(value = "/issue")
    public ResponseEntity<ApiResult<List<IssueStatisticsDto>>> index(
            @Parameter(description = "조회 시작일 (yyyy-MM-dd)", required = true)
            @RequestParam(name = "from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from ,
            @Parameter(description = "조회 종료일 (yyyy-MM-dd)", required = true)
            @RequestParam(name = "to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to ,
            @Parameter(description = "브랜치 아이디")
            @RequestParam(name = "branch_id",required = false) Long branchId,
            @Parameter(description = "팀 아이디")
            @RequestParam(name = "team_id",required = false) Long teamId,
            @Parameter(description = "사용자 아이디")
            @RequestParam(name = "member_id",required = false) Long memberId
    ) {
        List<IssueStatisticsDto> dto = issueStatisticsService.index(from ,to ,branchId , teamId , memberId);
        ApiResult<List<IssueStatisticsDto>> response = ApiResult.<List<IssueStatisticsDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(dto)
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @Tag(name = "이슈 통계 API")
    @Operation(summary = "이슈 통계 조회")
    @GetMapping(value = "/issue/sum")
    public ResponseEntity<ApiResult<IssueStatisticsDto>> sum(
            @Parameter(description = "조회 시작일 (yyyy-MM-dd)", required = true)
            @RequestParam(name = "from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from ,
            @Parameter(description = "조회 종료일 (yyyy-MM-dd)", required = true)
            @RequestParam(name = "to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to ,
            @Parameter(description = "브랜치 아이디")
            @RequestParam(name = "branch_id",required = false) Long branchId,
            @Parameter(description = "팀 아이디")
            @RequestParam(name = "team_id",required = false) Long teamId,
            @Parameter(description = "사용자 아이디")
            @RequestParam(name = "member_id",required = false) Long memberId
    ) {
        IssueStatisticsDto dto = issueStatisticsService.sum(from ,to ,branchId , teamId , memberId);
        ApiResult<IssueStatisticsDto> response = ApiResult.<IssueStatisticsDto>builder()
                .code(ApiResultCode.succeed)
                .payload(dto)
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }


}
