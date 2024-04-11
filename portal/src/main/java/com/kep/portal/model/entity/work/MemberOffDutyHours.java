package com.kep.portal.model.entity.work;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 회원 근무 예외 시간
 */
@Entity
@Table(
        indexes = {
                @Index(name = "IDX_MEMBER_OFF_DUTY_HOURS__SEARCH", columnList = "member_id"),
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_MEMBER_OFF_DUTY_HOURS", columnNames = {"member_id", "start_created","end_created"}
                )
        }
)
@DynamicInsert
@DynamicUpdate
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberOffDutyHours extends OffDutyHours {

    @Positive
    @NotNull
    @Column(name = "member_id")
    private Long memberId;

}
