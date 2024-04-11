package com.kep.core.model.dto.guide;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Long id;

    @NotNull
    private Long guideId;

    @NotNull
    private Long issueId;

    @NotNull
    private Long blockId;

    // guide contents 기준 배열 index
    @NotNull
    private Long contentId;

    @NotNull
    private Long creator;

    private ZonedDateTime created;
}
