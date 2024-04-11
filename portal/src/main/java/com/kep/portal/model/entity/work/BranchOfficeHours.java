package com.kep.portal.model.entity.work;

import com.kep.portal.event.BranchOfficeHoursEventListener;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@DynamicInsert
@DynamicUpdate
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(BranchOfficeHoursEventListener.class)
public class BranchOfficeHours extends OfficeHours {

    @Positive
    @Column(name = "branch_id" , unique = true)
    @NotNull
    private Long branchId;


}
