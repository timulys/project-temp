package com.kep.portal.controller.upload;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.upload.UploadHistoryDto;
import com.kep.core.model.type.QueryParam;
import com.kep.portal.model.dto.upload.UploadHistorySearchCondition;
import com.kep.portal.service.upload.UploadHistoryService;
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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Tag(name = "파일 업로드 이력 API", description = "/api/v1/upload-history")
@Slf4j
@RestController
@RequestMapping("/api/v1/upload-history")
public class FileHistoryController {

    @Resource
    private UploadHistoryService uploadHistoryService;

    @Tag(name = "파일 업로드 이력 API")
    @Operation(summary = "파일 업로드 이력 조회")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('READ_FILE_HISTORY') and hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<ApiResult<List<UploadHistoryDto>>> get(
            @QueryParam @Valid UploadHistorySearchCondition condition,
            @SortDefault.SortDefaults({
                    @SortDefault(sort = {"created"}, direction = Sort.Direction.DESC)}) Pageable pageable
            ){

        log.info("condition = {}",condition);

        Page<UploadHistoryDto> page = uploadHistoryService.searchOpen(condition, pageable);
        return response(page, HttpStatus.OK);

    }

    private ResponseEntity<ApiResult<List<UploadHistoryDto>>> response(@NotNull Page<UploadHistoryDto> page, HttpStatus httpStatus) {
        ApiResult<List<UploadHistoryDto>> response = ApiResult.<List<UploadHistoryDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(page.getContent())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .build();
        return new ResponseEntity<>(response, httpStatus);
    }

}
