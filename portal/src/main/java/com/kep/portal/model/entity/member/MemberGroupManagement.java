package com.kep.portal.model.entity.member;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_MEMBER_GROUP_MANAGEMENT", columnNames = {"memberGroupId", "memberId"})
        }
)
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MemberGroupManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberGroupId", foreignKey = @ForeignKey(name="FK_MEMBER_GROUP_MANAGEMENT__MEMBER_GROUP"))
    @Comment("유저-그룹 매칭 PK")
    private MemberGroup memberGroup;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", foreignKey = @ForeignKey(name="FK_MEMBER_GROUP_MANAGEMENT__MEMBER"))
    @Comment("유저 PK")
    private Member member;

    @Comment("사용 여부")
    private String enable;

    @Comment("생성 일시")
    private ZonedDateTime created;
}
