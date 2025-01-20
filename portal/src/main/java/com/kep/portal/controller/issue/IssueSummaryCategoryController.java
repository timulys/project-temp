package com.kep.portal.controller.issue;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.dto.issue.summary.IssueSummaryCategoryDto;
import com.kep.portal.model.dto.issue.summary.IssueSummaryCategoryResponse;
import com.kep.portal.model.dto.issue.summary.SaveIssueSummaryCategoryRequest;
import com.kep.portal.service.issue.IssueSummaryCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.nio.charset.StandardCharsets;


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
    public ResponseEntity<ApiResult<IssueSummaryCategoryDto>> getOne(
            @Parameter(description = "상담 후처리 카테고리 아이디 (최하위 뎁스 ID)", in = ParameterIn.PATH, required = true)
            @PathVariable("issueSummaryCategoryId") @Positive Long issueSummaryCategoryId
    ) {

        ApiResult<IssueSummaryCategoryDto> response = ApiResult.<IssueSummaryCategoryDto>builder()
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
    public ResponseEntity<ApiResult<String>> delete(@PathVariable("issueSummaryCategoryId") @Positive Long issueSummaryCategoryId) {
        issueSummaryCategoryService.delete(issueSummaryCategoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResult<>(ApiResultCode.succeed));
    }


    /**
     * TODO :: FileUtil 적용 시 리팩토링
     * @return
     */
    @Tag(name = "상담 요약(후처리) 관리 API")
    @Operation(summary = "요약(후처리) 템플릿 다운로드 (FileUtil 적용 이후 진행)")
    @GetMapping("/template")
    public ResponseEntity<Resource> downloadExcelTemplate() {
        Resource resource = issueSummaryCategoryService.downloadExcelTemplate();
        HttpHeaders headers = new HttpHeaders();

        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename(new String("상담_후처리_카테고리.csv".getBytes(), StandardCharsets.UTF_8), StandardCharsets.UTF_8)
                .build();

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(resource);
    }

    @Tag(name = "상담 요약(후처리) 관리 API")
    @Operation(summary = "요약(후처리) 카테고리 삭제(사용여부 X)")
    @PostMapping("/template")
    public ResponseEntity<ApiResult<String>> uploadExcelTemplate(@RequestPart("csv_file") MultipartFile file) {

        // TODO :: 예외 메시지, 파일 검증 등 모바일 1차 후 fileUtil 재설계 적용하면서 리팩토링
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("csv file must not be null or empty");
        if (file.getOriginalFilename() == null || !file.getOriginalFilename().toLowerCase().endsWith(".csv")) throw new IllegalArgumentException("only can be .csv file");

        issueSummaryCategoryService.saveExcel(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResult<>(ApiResultCode.succeed));
    }
}
