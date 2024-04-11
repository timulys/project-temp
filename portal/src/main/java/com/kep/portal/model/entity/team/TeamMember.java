package com.kep.portal.model.entity.team;

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
@Getter
@Setter
@Table(
        indexes = {
                @Index(name = "IDX_TEAM_MEMBER__TEAM", columnList = "teamId"),
                @Index(name = "IDX_TEAM_MEMBER__MEMBER", columnList = "memberId")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_TEAM_MEMBER__MEMBER_TEAM", columnNames = {"memberId", "teamId"})
        }
)
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Positive
    @Comment("PK")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teamId", foreignKey = @ForeignKey(name="FK_TEAM_MEMBER__TEAM"), nullable = false)
    @NotNull
    @Comment("소속 PK")
    private Team team;

    @Positive
    @NotNull
    @Comment("회원 PK")
    private Long memberId;

    @Positive
    @NotNull
    @Comment("생성 Member PK")
    private Long modifier;

    @NotNull
    @Comment("수정일")
    private ZonedDateTime modified;
}


