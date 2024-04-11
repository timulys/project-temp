/**
 *  고객별 유망 고객 테이블
 * @수정일자	  / 수정자			 / 수정내용
 * 2023.04.10 / asher.shin	 / 신규
 * 2023.04.14 / asher.shin  / 최신 채팅 정보 추가
 */
package com.kep.portal.model.entity.customer;

import com.kep.portal.model.entity.guide.GuideCategory;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomerPromise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")
    @Comment("고객 PK")
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    @Comment("상품 PK")
    private Product product;

    @Comment("등록 시간")
    @NotNull
    private ZonedDateTime created;

    @Comment("수정 시간")
    private ZonedDateTime modified;

    @Transient
    private Issue issue;
}
