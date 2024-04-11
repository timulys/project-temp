package com.kep.core.model.dto.issue;

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
public class IssueHighlightDto {

    @Positive
    private Long id;

    @NotNull
    private String keyword;

    private Long creator;

    private ZonedDateTime created;

    private Long modifier;

    private ZonedDateTime modified;
}
