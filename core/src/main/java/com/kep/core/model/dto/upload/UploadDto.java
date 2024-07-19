package com.kep.core.model.dto.upload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadDto {

    @Positive
    @Schema(description = "업로드 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotNull
    @Schema(description = "업로드 타입", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    @NotNull
    @Schema(description = "파일", requiredMode = Schema.RequiredMode.REQUIRED)
    private MultipartFile file;

    @Schema(description = "파일 원본명")
    private String originalName;

    @Schema(description = "파일명")
    private String name;

    @Schema(description = "경로")
    private String path;

    @Schema(description = "url")
    private String url;

    @Schema(description = "컨텐트 타입")
    private String mimeType;

    @Schema(description = "파일 사이즈")
    private Long size;

    @Positive
    @Schema(description = "생성자", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long creator;

    @Schema(description = "생성일시")
    private ZonedDateTime created;
}
