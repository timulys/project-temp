package com.kep.portal.model.entity.site;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    private Integer id;

    @Column(insertable = false)
    @Comment("이름")
    private String name;

    @Column(name = "team_count" , insertable = false)
    @ColumnDefault("0")
    @Comment("상담그룹 카운트")
    private Integer teamCount;

    @Column(name = "branch_count" , insertable = false)
    @ColumnDefault("0")
    @Comment("브랜치 카운트")
    private Integer branchCount;

    @Column(name = "member_count" , insertable = false)
    @ColumnDefault("0")
    @Comment("유저 카운트")
    private Integer memberCount;

    @Column(name = "channel_count" , insertable = false)
    @ColumnDefault("0")
    @Comment("채널 카운트")
    private Integer channelCount;

}
