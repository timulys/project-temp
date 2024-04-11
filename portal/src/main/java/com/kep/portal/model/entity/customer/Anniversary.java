/**
 *  기념일 코드 테이블
 * @수정일자	  / 수정자			 / 수정내용
 * 2023.04.10 / asher.shin	 / 신규
 */
package com.kep.portal.model.entity.customer;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Anniversary {

    @Id
    @Comment("기념일 코드")
    @Column(length=3)
    private String code;

    @Comment("기념일 명")
    @Column(length=50)
    private String name;




}
