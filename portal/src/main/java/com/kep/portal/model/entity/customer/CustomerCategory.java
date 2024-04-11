/**
 *  고객별 기념일 테이블
 * @수정일자	  / 수정자			 / 수정내용
 * 2023.04.17 / asher.shin	 / 신규
 */
package com.kep.portal.model.entity.customer;


import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Positive;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;

    @Comment("카테고리명")
    private String name;

    @Comment("카테고리 순서")
    private Long sort;
}
