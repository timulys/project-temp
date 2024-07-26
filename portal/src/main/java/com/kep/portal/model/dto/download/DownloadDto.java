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
@Schema(description = "파일 저장 관련 DTO")
public class DownloadDto {

    // @Schema(description = "파일 저장 경로", example = "/opt/storage/image/2024/07", required = true)
    @Schema(description = "파일 저장 경로", required = true)
    @NotNull
    private String filePath;

    //@Schema(description = "파일명", example = "2a88cf17-c809-413f-be66-50ec5b29b496.png", required = true)
    @Schema(description = "파일명", required = true)
    @NotNull
    private String fileName;

}
