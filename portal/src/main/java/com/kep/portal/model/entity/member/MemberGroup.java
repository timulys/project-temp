package com.kep.portal.model.entity.member;

import com.kep.portal.model.entity.team.Team;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_MEMBER_GROUP", columnNames = {"teamId", "memberId"})
        }
)
@DynamicInsert
@DynamicUpdate
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId", unique = true, foreignKey = @ForeignKey(name="FK_MEMBER_GROUP__TEAM"))
    @Comment("상담그룹 PK")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", unique = true, foreignKey = @ForeignKey(name="FK_MEMBER_GROUP__MEMBER"))
    @Comment("유저 PK")
    private Member member;

    @Comment("이름")
    private String name;

    @Comment("유저 카운트")
    private Integer memberCount;
}
