package com.kep.portal.controller.system;


import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.branch.BranchTeamDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.core.model.dto.system.SystemEventHistoryDto;
import com.kep.portal.model.entity.system.SystemEventHistory;
import com.kep.portal.service.system.SystemEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
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

@Tag(name = "시스템 히스토리 API", description = "/api/v1/system/history")
@Slf4j
@RestController
@RequestMapping("/api/v1/system/history")
public class HistoryController {

    @Resource
    private SystemEventService systemEventService;

    @Tag(name = "시스템 히스토리 API")
    @Operation(summary = "시스템 히스토리 목록 조회")
    @GetMapping(value = "/")
    public ResponseEntity<ApiResult<List<SystemEventHistoryDto>>> index(
            @Parameter(description = "조회 시작일(yyyy-MM-dd)", required = true)
            @RequestParam(name = "from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from
            ,@Parameter(description = "조회 종료일(yyyy-MM-dd)", required = true)
            @RequestParam(name = "to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to
            ,@Parameter(description = "브랜치 아이디")
            @RequestParam(name = "branch_id", required = false) Long branchId
            ,@Parameter(description = "사용자 아이디")
            @RequestParam(name = "member_id", required = false) Long memberId
            ,@Parameter(description = "팀 아이디")
            @RequestParam(name = "team_id", required = false) Long teamId
            ,@Parameter(description = "액션 목록(login //로그인\n" +
            ", member_create //상담원 생성\n" +
            ", member_update //상담원 수정\n" +
            ", member_password //상담원 비밀번호 변경\n" +
            ", system_counsel_work //근무 조건 설정\n" +
            ", system_counsel_set //상담 환경 설정\n" +
            ", system_channel //채널 설정 변경\n" +
            ", system_counsel_auto_message //채널 설정 변경\n" +
            ", system_counsel_distribution //상담 배분 설정\n" +
            ", system_counsel_off_duty //근무시간 예외\n" +
            ", system_counsel_member_max //최대 상담 건수 개별 설정)")
            @RequestParam(name = "action", required = false) List<SystemEventHistoryActionType> actions
            , @SortDefault.SortDefaults({
                    @SortDefault(sort = {"id"}, direction = Sort.Direction.DESC)}) Pageable pageable) {


        Page<SystemEventHistoryDto> page = systemEventService.index(from , to , branchId , teamId , memberId , actions , pageable);

        return new ResponseEntity<>(ApiResult.<List<SystemEventHistoryDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(page.getContent())
                .currentPage(page.getNumber())
                .totalPage(page.getTotalPages())
                .totalElement(page.getTotalElements())
                .build(), HttpStatus.OK);
    }

}
