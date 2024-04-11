/**
 * 자동메시지 템플릿 entity
 *
 *  @생성일자     / 만든사람			/ 수정내용
 *  2023.05.25 / asher.shin	/ 신규
 */
package com.kep.portal.model.entity.member;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberAutoMessageTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;

    @Comment("템플릿 구분")
    private int category;

    @Comment("템플릿명")
    private String title;

    @Column(length = 4000)
    @Comment("고객 일정 자동메시지 템플릿")
    private String payload;


}
