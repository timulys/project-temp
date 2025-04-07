package com.kep.portal.model.entity.upload;

import com.kep.core.model.dto.upload.UploadDto;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Upload {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;

    @Comment("업로드 타입")
    @NotNull
    private String type;

    @Comment("원본 이름")
    @NotEmpty
    private String originalName;

    @Comment("이름")
    @NotEmpty
    private String name;

    @Comment("로컬 경로")
    @NotEmpty
    private String path;

    @Comment("URL")
    @NotEmpty
    private String url;

    @Comment("MIME 타입")
    @NotEmpty
    private String mimeType;

    @Comment("바이트 사이즈")
    @NotNull
    @Column(name = "SIZES")
    private Long size;

    @Comment("생성자")
    @NotNull
    @Positive
    private Long creator;

    @Comment("생성 일시")
    @NotNull
    private ZonedDateTime created;

    public static Upload of(UploadDto uploadDto) {
        return Upload.builder()
                .type(uploadDto.getType())
                .originalName(uploadDto.getOriginalName())
                .name(uploadDto.getName())
                .path(uploadDto.getPath())
                .url(uploadDto.getUrl())
                .mimeType(uploadDto.getMimeType())
                .size(uploadDto.getSize())
                .creator(uploadDto.getCreator())
                .created(uploadDto.getCreated())
                .build();
    }
}
