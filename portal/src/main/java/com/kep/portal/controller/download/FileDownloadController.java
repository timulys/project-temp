package com.kep.portal.controller.download;

import com.kep.portal.model.dto.download.DownloadDto;
import com.kep.portal.service.download.DownloadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;

@Tag(name = "파일 다운로드 API", description = "/api/v1/download")
@Slf4j
@RestController
@RequestMapping("/api/v1/download")
@RequiredArgsConstructor
public class FileDownloadController {

    private final DownloadService downloadService;

    @Tag(name = "파일 다운로드 API")
    @Operation(summary = "파일 다운로드")
    @GetMapping
    public ResponseEntity<? super Resource> download(@ParameterObject @Valid DownloadDto downloadDto) {

        File file = downloadService.getFile(downloadDto);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = downloadService.getFileSystemResource(file);

        // Attachment header information and file name
        String contentDisposition = "attachment; filename=\"" + file.getName() + "\"";

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

}