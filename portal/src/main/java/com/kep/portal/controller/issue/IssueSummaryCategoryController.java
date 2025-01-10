package com.kep.portal.controller.issue;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.dto.issue.IssueSummaryCategoryResponse;
import com.kep.portal.model.dto.issue.SaveIssueSummaryCategoryRequest;
import com.kep.portal.service.issue.IssueSummaryCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Tag(name = "상담 요약(후처리) 관리 API", description = "/api/v1/issue/extra/summary/categories")
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/issue/extra/summary/categories")
@RestController
public class IssueSummaryCategoryController {

    private final IssueSummaryCategoryService issueSummaryCategoryService;

    @Tag(name = "상담 요약(후처리) 관리 API")
    @Operation(summary = "요약(후처리) 카테고리 전체 조회 (트리)")
    @GetMapping
    public ResponseEntity<ApiResult<IssueSummaryCategoryResponse>> getAll() {

        ApiResult<IssueSummaryCategoryResponse> response = ApiResult.<IssueSummaryCategoryResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(issueSummaryCategoryService.getAll())
                .build();

        return ResponseEntity.ok(response);
    }

    @Tag(name = "상담 요약(후처리) 관리 API")
    @Operation(summary = "요약(후처리) 카테고리 단건 조회 (트리)")
    @GetMapping("/{issueSummaryCategoryId}")
    public ResponseEntity<ApiResult<IssueSummaryCategoryResponse>> getOne(@PathVariable("issueSummaryCategoryId") Long issueSummaryCategoryId) {

        ApiResult<IssueSummaryCategoryResponse> response = ApiResult.<IssueSummaryCategoryResponse>builder()
                .code(ApiResultCode.succeed)
                .payload(issueSummaryCategoryService.getOne(issueSummaryCategoryId))
                .build();

        return ResponseEntity.ok(response);
    }

    @Tag(name = "상담 요약(후처리) 관리 API")
    @Operation(summary = "요약(후처리) 카테고리 저장")
    @PostMapping
    public ResponseEntity<ApiResult<String>> save(@RequestBody @Valid SaveIssueSummaryCategoryRequest requestDto) {
        issueSummaryCategoryService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResult<>(ApiResultCode.succeed));
    }

    @Tag(name = "상담 요약(후처리) 관리 API")
    @Operation(summary = "요약(후처리) 카테고리 삭제(사용여부 X)")
    @PatchMapping("/{issueSummaryCategoryId}")
    public ResponseEntity<ApiResult<String>> delete(@PathVariable("issueSummaryCategoryId") Long issueSummaryCategoryId) {
        issueSummaryCategoryService.delete(issueSummaryCategoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResult<>(ApiResultCode.succeed));
    }
}
