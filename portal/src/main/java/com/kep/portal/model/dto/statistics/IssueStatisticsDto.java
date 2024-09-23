package com.kep.portal.model.dto.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class IssueStatisticsDto {

    private LocalDate created;

    private IssueStatisticsStatus status;

    @Positive
    private Long open = 0L;

    @Positive
    private Long close = 0L;

    @Positive
    private Long ing = 0L;

    @Positive
    private Long chat = 0L;

    @Positive
    private Long ingAfterClose = 0L;

    private LocalDate from;
    private LocalDate to;
}
