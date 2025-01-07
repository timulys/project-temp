package com.kep.core.model.dto.upload;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.customer.GuestDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.subject.IssueCategoryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadHistoryDto {

    @Schema(description = "업로드 히스토리 아이디(PK)")
    @Positive
    private Long id;

    @Schema(description = "이슈 아이디")
    @Positive
    private Long issueId;

    @Schema(description = "이슈 카테고리 정보")
    private IssueCategoryDto issueCategory;

    @Schema(description = "게스트 고객 정보")
    private GuestDto guest;

    @NotNull
    private String questName;

    @Schema(description = "파일 이름")
    @NotNull
    private String fileName;

    @Schema(description = "파일 확장자")
    @NotNull
    private String mimeType;

    @Schema(description = "파일 용량")
    @NotNull
    private Long size;

    @Schema(description = "파일 url")
    @NotNull
    private String url;

    @Schema(description = "팀 아이디")
    @NotNull
    private Long teamId;

    @Schema(description = "생성자 정보")
    @NotNull
    @JsonIncludeProperties({"id","username","nickname"})
    private MemberDto creator;

    @Schema(description = "생성 일자")
    private ZonedDateTime created;
}
