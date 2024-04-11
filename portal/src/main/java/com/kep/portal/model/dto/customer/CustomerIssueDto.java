/**
 * 고객목록 조회시 채팅정보용 DTO
 *  @생성일자      / 만든사람		 	/ 수정내용
 * 	 2023.04.12 / asher.shin   / 신규
 */
package com.kep.portal.model.dto.customer;

import com.kep.core.model.dto.issue.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerIssueDto {


    private Long id;

    private IssueStatus status;

    private Long issueSessionDay;

    private String issueSessionDDay;
}
