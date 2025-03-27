package com.kep.portal.controller.issue;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.issue.IssueCloseType;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.portal.model.dto.issue.response.PostCustomerSyncResponseDto;
import com.kep.portal.service.issue.event.EventByOperatorService;
import com.kep.portal.service.issue.event.OperatorEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.Table;
import java.util.Map;

/**
 * 상담원 이벤트 중 실시간 채팅 상담
 *
 * <ul>
 * <li>메세지
 * <li>접수 (첫 메세지로 대응)
 * <li>상담 종료
 * <li>파일 업로드
 * </ul>
 */
@Tag(name = "이벤트 API [오퍼레이터]", description = "/api/v1/issue/event-by-operator")
@Slf4j
@RestController
@RequestMapping(("/api/v1/issue/{issueId}/event-by-operator"))
@RequiredArgsConstructor
public class EventByOperatorController {
    @Resource
    private EventByOperatorService eventByOperatorService;
    private final OperatorEventService operatorEventService;

    /**
     * 메세지
     */
    @Tag(name = "이벤트 API [오퍼레이터]")
    @Operation(summary = "메시지")
    @PostMapping(value = "/message")
    @PreAuthorize("hasAnyAuthority('WRITE_ISSUE')")
    public ResponseEntity<ApiResult<IssueDto>> message(
            @Parameter(description = "이슈 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable("issueId") Long issueId,
            @RequestBody IssuePayload issuePayload) throws Exception {

        log.info("EVENT BY OPERATOR, MESSAGE, ID: {}, BODY: {}", issueId, issuePayload);

        eventByOperatorService.message(issueId, issuePayload);

        return new ResponseEntity<>(ApiResult.<IssueDto>builder()
                .code(ApiResultCode.succeed)
                .build(), HttpStatus.CREATED);
    }

    /**
     * 상담 종료
     */
    @Tag(name = "이벤트 API [오퍼레이터]")
    @Operation(summary = "상담 종료")
    @PostMapping(value = "/close")
    @PreAuthorize("hasAnyAuthority('WRITE_ISSUE')")
    public ResponseEntity<ApiResult<IssueDto>> close(
            @PathVariable("issueId") Long issueId,
            @RequestBody(required = false) Map<String, Object> options) throws Exception {

        log.info("EVENT BY OPERATOR, CLOSE, ID: {}, OPTIONS: {}", issueId, options);

        // TODO: 종료 프로세스 (상담원 종료)
        IssueDto issueDto = eventByOperatorService.warningClose(issueId, options);

        return new ResponseEntity<>(ApiResult.<IssueDto>builder()
                .code(ApiResultCode.succeed)
                .payload(issueDto)
                .build(), HttpStatus.CREATED);
    }

    /**
     * 파일 업로드 (파일 업로드 후 URL 리턴)
     * TODO: 플랫폼 업로드, 솔루션 업로드 분리
     */
    @Tag(name = "이벤트 API [오퍼레이터]")
    @Operation(summary = "파일 업로드 후 URL 리턴")
    @PostMapping(value = "/upload")
    @PreAuthorize("hasAnyAuthority('WRITE_ISSUE')")
    public ResponseEntity<ApiResult<String>> upload(
            @PathVariable("issueId") Long issueId,
            @RequestParam("file") MultipartFile file) throws Exception {

        log.info("EVENT BY OPERATOR, FILE, ID: {}, NAME: {}, SIZE: {}",
                issueId, file.getOriginalFilename(), file.getSize());

        String uploadUrl = eventByOperatorService.upload(issueId, file);

        if (!ObjectUtils.isEmpty(uploadUrl)) {
            return new ResponseEntity<>(ApiResult.<String>builder()
                    .code(ApiResultCode.succeed)
                    .payload(uploadUrl)
                    .build(), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(ApiResult.<String>builder()
                .code(ApiResultCode.failed)
                .build(), HttpStatus.NOT_ACCEPTABLE);
    }


    /**
     * 강제종료 신규추가
     *
     *  @생성일자      / 만든사람		 	/ 수정내용
     * 	 2023.05.09 / philip.lee   / 
     */
    @Tag(name = "이벤트 API [오퍼레이터]")
    @Operation(summary = "상담 강제종료")
    @PostMapping(value = "/force/close")
    //@PreAuthorize("hasAnyAuthority('WRITE_ISSUE')")
    public ResponseEntity<ApiResult<IssueDto>> forcedClose(
            @PathVariable("issueId") Long issueId,
            @RequestBody(required = false) Map<String, Object> options) throws Exception {

        log.info("EVENT BY OPERATOR, CLOSE, ID: {}, OPTIONS: {}", issueId, options);

        // TODO: 종료 프로세스 (상담원 종료)
        IssueDto issueDto = eventByOperatorService.close(issueId, options);

        return new ResponseEntity<>(ApiResult.<IssueDto>builder()
                .code(ApiResultCode.succeed)
                .payload(issueDto)
                .build(), HttpStatus.CREATED);
    }

    /** V2 Apis **/
    @Tag(name = "이벤트 API [오퍼레이터]")
    @Operation(summary = "고객 인증 요청 메시지 송신")
    @PostMapping("/sync")
    public ResponseEntity<? super PostCustomerSyncResponseDto> customerSync(@PathVariable("issueId") Long issueId) {
        log.info("Request Customer Kakao Sync, Issue ID : {}", issueId);
        ResponseEntity<? super PostCustomerSyncResponseDto> response = operatorEventService.customerSyncRequest(issueId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
