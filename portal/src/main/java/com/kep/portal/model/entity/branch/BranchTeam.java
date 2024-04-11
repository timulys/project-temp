package com.kep.portal.model.entity.branch;


import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.team.Team;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(
        indexes = {
            @Index(name = "IDX_BRANCH_TEAM__BRANCH", columnList = "branchId")
        },
        uniqueConstraints = {
            @UniqueConstraint(name = "UK_BRANCH_TEAM__BRANCH", columnNames = { "branchId", "teamId" })
        }
)
public class BranchTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;


    @ManyToOne
    @JoinColumn(name = "branchId", foreignKey = @ForeignKey(name="FK_BRANCH_TEAM__BRANCH"), nullable = false, updatable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "teamId", foreignKey = @ForeignKey(name="FK_BRANCH_TEAM__TEAM"), nullable = false, updatable = false)
    @NotNull
    @Comment("소속 PK")
    private Team team;

    @NotNull
    @Comment("그룹장 PK")
    @ManyToOne
    @JoinColumn(name = "memberId", foreignKey = @ForeignKey(name="FK_BRANCH_TEAM__MEMBER"), nullable = false)
    private Member member;

    @Comment("생성자")
    @Positive
    @NotNull
    private Long creator;

    @Comment("생성 일시")
    @NotNull
    private ZonedDateTime created;

}
