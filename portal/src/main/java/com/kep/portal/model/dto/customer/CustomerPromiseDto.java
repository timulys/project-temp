
/**
 * 유망 고객 dto
 *  @생성일자      / 만든사람		 	/ 수정내용
 * 	 2023.04.12 / asher.shin   / 신규
 * 	 2023.04.14 / asher.shin   /최신 채팅정보 추가
 */
package com.kep.portal.model.dto.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.guide.GuideCategory;
import com.kep.portal.model.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPromiseDto {


    private Long id;


    private CustomerResponseDto customer;


    private Product product;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private ZonedDateTime created;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private ZonedDateTime modified;

    private CustomerIssueDto issue;
}
