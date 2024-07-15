package com.kep.portal.controller.upload;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.upload.UploadDto;
import com.kep.portal.service.upload.UploadService;
import com.kep.portal.util.UploadUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.File;

@Tag(name = "파일 업로드 API", description = "/api/v1/upload")
@Slf4j
@RestController
@RequestMapping("/api/v1/upload")
public class FileController {

    @Resource
    private UploadService uploadService;
    @Resource
    private UploadUtils uploadUtils;

    @Tag(name = "파일 업로드 API")
    @Operation(summary = "파일 업로드 정보 조회")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResult<UploadDto>> get(
            @Parameter(description = "업로드 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "id") Long id) {

        UploadDto upload = uploadService.getById(id);
        ApiResult<UploadDto> response = ApiResult.<UploadDto>builder()
                .code(ApiResultCode.succeed)
                .payload(upload)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Tag(name = "파일 업로드 API")
    @Operation(summary = "파일 업로드 정보 저장")
    @PostMapping
    public ResponseEntity<ApiResult<UploadDto>> upload(@NotNull UploadDto dto) throws Exception {

        File file = uploadUtils.upload(dto.getFile());
        Assert.notNull(file, "failed upload");

        dto.setOriginalName(dto.getFile().getOriginalFilename());
        dto.setMimeType(uploadUtils.getMimeType(dto.getFile()));
        log.info("Upload: {}", dto);

        UploadDto uploadDto = uploadService.store(dto , file);

        ApiResult<UploadDto> response = ApiResult.<UploadDto>builder()
                .code(ApiResultCode.succeed)
                .payload(uploadDto)
                .build();
        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }
}
