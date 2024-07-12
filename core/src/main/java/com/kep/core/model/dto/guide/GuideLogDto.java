package com.kep.core.model.dto.guide;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GuideLogDto {

    @NotNull
    @Schema(description = "가이드 로그 아이디")
    private Long id;

    @NotNull
    @Schema(description = "가이드 아이디")
    private Long guideId;

    @NotNull
    @Schema(description = "이슈 아이디")
    private Long issueId;

    @NotNull
    @Schema(description = "블록 아이디")
    private Long blockId;

    // guide contents 기준 배열 index
    @NotNull
    @Schema(description = "가이드 컨텐트 아이디 (guide contents 기준 배열 index)")
    private Long contentId;

    @NotNull
    @Schema(description = "생성자")
    private Long creator;

    @Schema(description = "생성일시")
    private ZonedDateTime created;
}
