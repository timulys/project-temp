
/**
 *
 *  @생성일자      / 만든사람		 	/ 수정내용
 * 	 2023.04.12 / asher.shin   /  고개 카테고리 Response Dto
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
public class CustomerCommonResponseDto {

    //고객 PK
    private Long id;

    private  String name;

    private String address;

    private boolean favorites;

    private String anniversaryCode;
    private Long issueId;

    private String url;

    private String sessionExpireDate;

    private Long issueSessionDay;

    private IssueStatus status;

    private String content;

    private String phoneNumber;

    private Long chatCount;

    private Long customerId;

    private boolean friendTalkSucceed;

    private Long categoryId;

    private String categoryName;

    private Long order;

}
