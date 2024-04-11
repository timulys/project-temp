/**
 *  고객별 계약상품 테이블
 * @수정일자	  / 수정자			 / 수정내용
 * 2023.04.10 / asher.shin	 / 신규
 */
package com.kep.portal.model.entity.customer;

import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.product.Product;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerContract {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="customer_id")
    @Comment("고객 PK")
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="member_id")
    @Comment("계약진행 직원 PK")
    private Member member;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="product_id")
    @Comment("상품 PK")
    private Product product;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Comment("계약 시작 날짜")
    private LocalDate contractStartDate;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Comment("계약 종료 날짜")
    private LocalDate contractEndDate;


    @Comment("계약여부")//계약/가계약
    private boolean contracted;

    @Comment("해지여부")
    private boolean canceled;

    @Comment("계약해지날짜")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate canceledDate;

    @Comment("등록날짜")
    private ZonedDateTime created;

    @Comment("수정날짜")
    private ZonedDateTime modified;

    @Comment("월 보험료")
    private Long monthlyPremium;

    @Transient
    private Issue issue;


}
