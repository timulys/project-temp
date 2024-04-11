package com.kep.portal.model.entity.branch;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kep.portal.model.converter.BooleanConverter;
import com.kep.portal.model.entity.channel.Channel;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * {@link Branch}, {@link Channel} 매칭
 */
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
                @Index(name = "IDX_BRANCH_CHANNEL__BRANCH", columnList = "branchId"),
                @Index(name = "IDX_BRANCH_CHANNEL__CHANNEL", columnList = "channelId,owned")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_BRANCH_CHANNEL", columnNames = { "branchId", "channelId" })
        }
)
public class BranchChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branchId", foreignKey = @ForeignKey(name = "FK_BRANCH_CHANNEL__BRANCH"),  nullable = false, updatable = false)
    @Comment("브랜치 PK")
    @NotNull
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "channelId", foreignKey = @ForeignKey(name = "FK_CHANNEL_BRANCH__CHANNEL"),  nullable = false, updatable = false)
    @Comment("채널 PK")
    @NotNull
    @JsonBackReference
    private Channel channel;

    @Convert(converter = BooleanConverter.class)
    @Column(length = 1)
    @ColumnDefault("'N'")
    @Comment("메인 브랜치 여부")
    private Boolean owned;

    @Comment("생성 회원 PK")
    @NotNull
    private Long creator;

    @Comment("생성일")
    @NotNull
    private ZonedDateTime created;
}
