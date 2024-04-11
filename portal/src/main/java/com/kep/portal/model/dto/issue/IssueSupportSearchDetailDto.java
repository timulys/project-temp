package com.kep.portal.model.dto.issue;

import com.kep.core.model.dto.customer.GuestDto;
import com.kep.core.model.dto.issue.IssueLogDto;
import com.kep.core.model.dto.issue.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 상담지원요청 상세
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueSupportSearchDetailDto {
    private List<IssueLogDto> issueLog;

    private GuestDto guest;

    private IssueSupportDetailDto supportInfo;

    private IssueStatus issueStatus;
}
