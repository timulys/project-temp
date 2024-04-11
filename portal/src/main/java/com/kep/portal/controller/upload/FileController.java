package com.kep.portal.controller.upload;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.upload.UploadDto;
import com.kep.portal.service.upload.UploadService;
import com.kep.portal.util.UploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.File;

@Slf4j
@RestController
@RequestMapping("/api/v1/upload")
public class FileController {

    @Resource
    private UploadService uploadService;
    @Resource
    private UploadUtils uploadUtils;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResult<UploadDto>> get(
            @PathVariable(value = "id") Long id) {

        UploadDto upload = uploadService.getById(id);
        ApiResult<UploadDto> response = ApiResult.<UploadDto>builder()
                .code(ApiResultCode.succeed)
                .payload(upload)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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
