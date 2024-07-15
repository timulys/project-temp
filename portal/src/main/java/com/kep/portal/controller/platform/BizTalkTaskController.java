package com.kep.portal.controller.platform;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.platform.BizTalkTaskDto;
import com.kep.core.model.type.QueryParam;
import com.kep.portal.model.dto.platform.BizTalkTaskCondition;
import com.kep.portal.model.entity.platform.BizTalkTask;
import com.kep.portal.service.platform.BizTalkTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Tag(name = "비즈톡 예약 관리 API", description = "/api/v1/platform/biztalk-task")
@Slf4j
@RestController
@RequestMapping("/api/v1/platform/biztalk-task")
public class BizTalkTaskController {

    @Resource
    private BizTalkTaskService bizTalkTaskService;

    /**
     * 상담관리에서 예약 목록 조회
     */
    @Tag(name = "비즈톡 예약 관리 API")
    @Operation(summary = "상담관리에서 예약 목록 조회")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('WRITE_TALK_TASK')")
    public ResponseEntity<ApiResult<List<BizTalkTaskDto>>> search(
            @QueryParam BizTalkTaskCondition condition,
            @SortDefault.SortDefaults({
                    @SortDefault(sort = {"created"}, direction = Sort.Direction.DESC)}) Pageable pageable
    ) {

        log.info("BIZ TALK CONTROLLER SEARCH CONDITION: {}", condition);

        Page<BizTalkTaskDto> page = bizTalkTaskService.search(condition, pageable);

        ApiResult<List<BizTalkTaskDto>> result = ApiResult.<List<BizTalkTaskDto>>builder()
                .payload(page.getContent())
                .code(ApiResultCode.succeed)
                .currentPage(page.getNumber())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 포털에서 상담원이 예약 목록 조회
     */
    @Tag(name = "비즈톡 예약 관리 API")
    @Operation(summary = "포털에서 상담원이 예약 목록 조회")
    @GetMapping("/search")
    public ResponseEntity<ApiResult<List<BizTalkTaskDto>>> memberSearch(
            @QueryParam BizTalkTaskCondition condition,
            @SortDefault.SortDefaults({
                    @SortDefault(sort = {"created"}, direction = Sort.Direction.DESC)}) Pageable pageable
    ) {

        log.info("BIZ TALK CONTROLLER SEARCH CONDITION: {}", condition);

        Page<BizTalkTaskDto> page = bizTalkTaskService.search(condition, pageable);

        ApiResult<List<BizTalkTaskDto>> result = ApiResult.<List<BizTalkTaskDto>>builder()
                .payload(page.getContent())
                .code(ApiResultCode.succeed)
                .currentPage(page.getNumber())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * 상담관리에서 톡 전송 예약 취소
     */
    @PutMapping
    @Tag(name = "비즈톡 예약 관리 API")
    @Operation(summary = "상담관리에서 톡 전송 예약 취소")
    @PreAuthorize("hasAnyAuthority('WRITE_TALK_TASK')")
    public ResponseEntity<ApiResult<Map<String, Integer>>> cancel(
            @Schema(description = "비즈톡 예약 아이디 목록")
            @RequestBody List<Long> ids
    ) {
        log.info("BIZ TALK CONTROLLER REQUEST CANCEL ID LIST: {}", ids);

        Map<String, Integer> result = bizTalkTaskService.setTaskCancel(ids);
        ApiResult<Map<String, Integer>> response = ApiResult.<Map<String, Integer>>builder()
                .code(ApiResultCode.succeed)
                .payload(result)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 상담원이 톡 전송 예약 취소
     */
    @Tag(name = "비즈톡 예약 관리 API")
    @Operation(summary = "상담원이 톡 전송 예약 취소")
    @PutMapping("/cancel")
    public ResponseEntity<ApiResult<Map<String, Integer>>> memberCancel(
            @Schema(description = "비즈톡 예약 아이디 목록")
            @RequestBody List<Long> ids
    ) {
        log.info("BIZ TALK CONTROLLER REQUEST CANCEL ID LIST: {}", ids);

        Map<String, Integer> result = bizTalkTaskService.memberTaskCancel(ids);
        ApiResult<Map<String, Integer>> response = ApiResult.<Map<String, Integer>>builder()
                .code(ApiResultCode.succeed)
                .payload(result)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
