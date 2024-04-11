package com.kep.portal.controller.system;


import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.branch.BranchTeamDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.core.model.dto.system.SystemEventHistoryDto;
import com.kep.portal.model.entity.system.SystemEventHistory;
import com.kep.portal.service.system.SystemEventService;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/system/history")
public class HistoryController {

    @Resource
    private SystemEventService systemEventService;

    @GetMapping(value = "/")
    public ResponseEntity<ApiResult<List<SystemEventHistoryDto>>> index(@RequestParam(name = "from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from
            , @RequestParam(name = "to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to
            , @RequestParam(name = "branch_id", required = false) Long branchId
            , @RequestParam(name = "member_id", required = false) Long memberId
            , @RequestParam(name = "team_id", required = false) Long teamId
            , @RequestParam(name = "action", required = false) List<SystemEventHistoryActionType> actions
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
