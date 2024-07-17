package com.kep.portal.controller.issue;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.platform.kakao.KakaoBizTalkSendResponse;
import com.kep.portal.service.platform.BizTalkHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 플랫폼에서 전달된 비즈톡 콜백 이벤트
 */
@Tag(name = "플랫폼 비즈톡 콜백 이벤트 API", description = "/api/v1/event-by-platform/biztalk")
@RestController
@RequestMapping("/api/v1/event-by-platform/biztalk")
@Slf4j
public class EventByPlatformBizTalkController {

    @Resource
    private BizTalkHistoryService bizTalkHistoryService;

    /**
     * 메세지 콜백 이벤트
     */
    @Tag(name = "플랫폼 비즈톡 콜백 이벤트 API")
    @Operation(summary = "콜백")
    @PutMapping(value = "/callback")
    public ResponseEntity<ApiResult<String>> biztalkCallback(
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") Long trackKey,
            @RequestBody KakaoBizTalkSendResponse dto) throws Exception {

        log.info("EVENT BY PLATFORM, BIZTALK CALLBACK, TRACK KEY: {}, DTO: {}",
                trackKey, dto);

        bizTalkHistoryService.modify(dto);

        return new ResponseEntity<>(ApiResult.<String>builder()
                .code(ApiResultCode.succeed)
                .build(), HttpStatus.OK);
    }
}
