package com.kep.portal.model.entity.customer;

import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.product.Product;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerInterested {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
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
    @Comment("등록날짜")
    private LocalDate created;


}
