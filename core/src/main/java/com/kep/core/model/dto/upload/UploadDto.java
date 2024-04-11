package com.kep.core.model.dto.upload;

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
    private Long id;

    @NotNull
    private String type;

    @NotNull
    private MultipartFile file;

    private String originalName;

    private String name;

    private String path;

    private String url;

    private String mimeType;

    private Long size;

    @Positive
    private Long creator;

    private ZonedDateTime created;
}
