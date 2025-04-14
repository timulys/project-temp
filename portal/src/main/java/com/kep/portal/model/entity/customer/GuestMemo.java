/**
 * Guest Memo Entity 신규
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.04.06 / asher.shin   / 신규
 */
package com.kep.portal.model.entity.customer;

import lombok.*;
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
@Setter
@Table(
        name="guest_memo"
)
public class GuestMemo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    private Long id;

    @Comment("고객 PK")
    private Long guestId;

    @Comment("상담원 PK")
    private Long memberId;

    @Comment("고객PK")
    private Long customerId;

    @Comment("메모내용")
    @Column(length=1000)
    private String content;

    @Comment("등록 시간")
    @NotNull
    private ZonedDateTime created;

    @Comment("수정 시간")
    private ZonedDateTime modified;
}
