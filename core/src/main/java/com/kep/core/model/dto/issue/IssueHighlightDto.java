package com.kep.core.model.dto.issue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Schema(description = "이슈 하이라이트 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueHighlightDto {

    @Positive
    @Schema(description = "이슈 하이라이트 아이디")
    private Long id;

    @NotNull
    @Schema(description = "키워드")
    private String keyword;

    @Schema(description = "생성자")
    private Long creator;

    @Schema(description = "생성일시")
    private ZonedDateTime created;

    @Schema(description = "수정자")
    private Long modifier;

    @Schema(description = "수정일시")
    private ZonedDateTime modified;
}
