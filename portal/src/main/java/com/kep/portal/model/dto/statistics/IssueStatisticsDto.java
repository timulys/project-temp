package com.kep.portal.model.dto.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
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
    private Long open;

    @Positive
    private Long close;

    @Positive
    private Long ing;

    @Positive
    private Long chat;

    private LocalDate from;
    private LocalDate to;
}
