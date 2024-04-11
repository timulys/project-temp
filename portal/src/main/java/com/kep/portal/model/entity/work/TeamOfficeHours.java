package com.kep.portal.model.entity.work;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Table(
        indexes = {@Index(name = "IDX_TEAM_OFFICE_HOURS__SEARCH", columnList = "team_id")}
)
@DynamicInsert
@DynamicUpdate
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Deprecated
public class TeamOfficeHours extends OfficeHours{

    @Positive
    @Column(name = "team_id")
    private Long teamId;

}
