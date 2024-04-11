/**
 * 고객 상품 테이블 추가
 *  @생성일자      / 만든사람		 	/ 수정내용
 * 	 2023.04.14 / asher.shin   / 신규
 */

package com.kep.portal.model.entity.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kep.portal.model.converter.BooleanConverter;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {




        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Positive
        @Comment("PK")
        private Long id;

        @NotEmpty
        @Comment("이름")
        private String name;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_id")
        @JsonBackReference
        @Comment("부모 카테고리 PK")
        private Product parent;

        @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
        @JsonManagedReference
        private List<Product> children;

        @Column(length = 1)
        @Convert(converter = BooleanConverter.class)
        @Builder.Default
        @Comment("브랜치 전체 오픈")
        private Boolean isOpen = false;

        @NotNull
        @Positive
        @Comment("카테고리 depth")
        private Integer depth;



    }


