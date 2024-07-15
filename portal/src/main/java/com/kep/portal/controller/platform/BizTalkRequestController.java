package com.kep.portal.controller.platform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.platform.BizTalkRequestDto;
import com.kep.core.model.dto.platform.kakao.KakaoBizTalkSendResponse;
import com.kep.core.model.type.QueryParam;
import com.kep.portal.model.dto.platform.BizTalkRequestCondition;
import com.kep.portal.service.platform.BizTalkRequestService;
import com.kep.portal.util.SecurityUtils;
import com.mchange.rmi.NotAuthorizedException;
import io.swagger.v3.oas.annotations.Operation;
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
import java.time.ZonedDateTime;
import java.util.List;

@Tag(name = "비즈톡 요청 API", description = "/api/v1/platform/biztalk-request")
@Slf4j
@RestController
@RequestMapping("/api/v1/platform/biztalk-request")
public class BizTalkRequestController {
    @Resource
    private BizTalkRequestService bizTalkRequestService;

    /**
     * 상담관리에서 매니저가 요청목록 조회
     */
    @Tag(name = "비즈톡 요청 API")
    @Operation(summary = "상담관리 > 매니저 요청 목록 조회")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('WRITE_TALK')")
    public ResponseEntity<ApiResult<List<BizTalkRequestDto>>> get(
            @QueryParam BizTalkRequestCondition condition, @SortDefault.SortDefaults({
            @SortDefault(sort = {"created"}, direction = Sort.Direction.DESC)}) Pageable pageable
    ) {
        log.info("condition = {}", condition);
        Page<BizTalkRequestDto> page = bizTalkRequestService.search(condition, pageable);

        ApiResult<List<BizTalkRequestDto>> result = ApiResult.<List<BizTalkRequestDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(page.getContent())
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalElement(page.getTotalElements())
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 포탈에서 상담원이 알림톡 전송
     */
    @Tag(name = "비즈톡 요청 API")
    @Operation(summary = "포탈에서 상담원이 알림톡 전송")
    @PostMapping("/alert")
    @PreAuthorize("hasAnyAuthority('WRITE_KAKAO_ALERT_TALK')")
    public ResponseEntity<ApiResult<String>> postAlert(
            @RequestBody BizTalkRequestDto dto
    ) {
        try {
            ApiResult<String> result = ApiResult.<String>builder()
                    .code(ApiResultCode.succeed)
                    .message(bizTalkRequestService.store(dto))
                    .build();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("ALERT TALK ERROR: {}",e.getLocalizedMessage(), e);
            ApiResult<String> result = ApiResult.<String>builder()
                    .code(ApiResultCode.failed)
                    .message("알림톡 발송에 실패하였습니다")
                    .build();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * 포탈에서 상담원이 친구톡 전송
     */
    @Tag(name = "비즈톡 요청 API")
    @Operation(summary = "포탈에서 상담원이 친구톡 전송")
    @PostMapping("friend")
    @PreAuthorize("hasAnyAuthority('WRITE_KAKAO_FRIEND_TALK')")
    public ResponseEntity<ApiResult<String>> postFriend(
            @RequestBody BizTalkRequestDto dto
    ) {
        try {
            ApiResult<String> result = ApiResult.<String>builder()
                    .code(ApiResultCode.succeed)
                    .message(bizTalkRequestService.store(dto))
                    .build();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("FRIEND TALK ERROR: {}",e.getLocalizedMessage(), e);
            ApiResult<String> result = ApiResult.<String>builder()
                    .code(ApiResultCode.failed)
                    .message("친구톡 발송에 실패하였습니다")
                    .build();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * 상담관리에서 톡 요청 승인
     */
    @Tag(name = "비즈톡 요청 API")
    @Operation(summary = "상담관리에서 톡 요청 승인")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('WRITE_TALK')")
    public ResponseEntity<ApiResult<KakaoBizTalkSendResponse>> put(
            @RequestBody BizTalkRequestDto dto
    ) throws JsonProcessingException {
        KakaoBizTalkSendResponse save = bizTalkRequestService.modify(dto);

        ApiResult<KakaoBizTalkSendResponse> result = ApiResult.<KakaoBizTalkSendResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(save)
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
