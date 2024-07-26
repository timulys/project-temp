package com.kep.portal.model.dto.download;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadDto {

    @Schema(description = "파일 저장 경로")
    @NotNull
    private String filePath;

    @Schema(description = "파일명")
    @NotNull
    private String fileName;

}
