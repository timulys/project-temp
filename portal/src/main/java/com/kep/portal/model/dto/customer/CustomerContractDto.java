/**
 * 고객 계약 DTO
 *  @생성일자      / 만든사람		 	/ 수정내용
 * 	 2023.04.12 / asher.shin   / 신규
 * 	 2023.04.14 / ahser.shin   / 최신 채팅 정보 추가
 */
package com.kep.portal.model.dto.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.guide.GuideCategory;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerContractDto {

    private Long id;


    @JsonIncludeProperties({"id","name"})
    private CustomerResponseDto customer;


    @JsonIncludeProperties({"id","nickname"})
    private Member member;


    private GuideCategory guideCategory;


    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate contractStartDate;



    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate contractEndDate;

    private boolean contracted;


    private boolean canceled;

    private Product product;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate canceledDate;

    private Long monthlyPremium;

    private CustomerIssueDto issue;

}
