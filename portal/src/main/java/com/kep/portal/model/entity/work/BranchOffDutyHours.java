package com.kep.portal.model.entity.work;

import com.kep.portal.event.BranchOffDutyHoursEventListener;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 브랜치 근무 예외 시간
 */
@Entity
@Table(
        indexes = {@Index(name = "IDX_BRANCH_OFF_DUTY_HOURS__SEARCH", columnList = "branch_id,start_created")},
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_BRANCH_OFF_DUTY_HOURS", columnNames = {"branch_id", "start_created","end_created"}
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
@EntityListeners(BranchOffDutyHoursEventListener.class)
public class BranchOffDutyHours extends OffDutyHours {

    @Positive
    @NotNull
    @Column(name = "branch_id")
    private Long branchId;
}
