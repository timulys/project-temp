package com.kep.portal.controller.download;

import com.kep.core.model.dto.download.DownloadDto;
import com.kep.portal.service.download.DownloadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.File;

@Tag(name = "파일 다운로드 API", description = "/api/v1/download")
@Slf4j
@RestController
@RequestMapping("/api/v1/download")
@AllArgsConstructor
public class FileDownloadController {

    private final DownloadService downloadService;

    @Tag(name = "파일 다운로드 API")
    @Operation(summary = "파일 다운로드")
    @GetMapping
    public ResponseEntity<Resource> download(@NotNull DownloadDto downloadDto) {

        File file = downloadService.getFile(downloadDto);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = downloadService.getFileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))
                .body(resource);
    }
}