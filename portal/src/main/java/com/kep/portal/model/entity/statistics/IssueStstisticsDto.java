package com.kep.portal.model.entity.statistics;

import com.kep.core.model.dto.issue.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueStstisticsDto {

    @Positive
    private Long id;

    private ZonedDateTime created;

    private ZonedDateTime firstTalkDateTime;

    private ZonedDateTime lastTalkDateTime;

    private String customerName;

    private String memberName;

    private IssueStatus issueStatus;

    private String waitDateTime;

    private String counselDateTime;

    private String issueCategoryName;

    private String issueLogLastPayload;

}
