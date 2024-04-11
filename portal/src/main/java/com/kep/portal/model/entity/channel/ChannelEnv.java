package com.kep.portal.model.entity.channel;


import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.portal.event.ChannelEnvEventListener;
import com.kep.portal.model.converter.BooleanConverter;
import com.kep.portal.model.converter.IssuePayloadConverter;
import com.kep.portal.model.entity.system.SystemIssuePayload;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.ZonedDateTime;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//TODO : 인터셉트 활용
//@EntityListeners(ChannelEnvEventListener.class)
public class ChannelEnv {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Positive
    private Long id;

    @OneToOne
    @JoinColumn(name = "channelId", foreignKey = @ForeignKey(name = "FK_CHANNEL_ENV__CHANNEL"), nullable = false, updatable = false)
    @Comment("채널 PK")
    @NotNull
    private Channel channel;

    @OneToOne
    @JoinColumn(name = "channelStartAutoId", foreignKey = @ForeignKey(name = "FK_CHANNEL_ENV__CHANNEL_START_AUTO"), nullable = false, updatable = false)
    @Comment("상담시작 종료 PK")
    @NotNull
    private ChannelStartAuto start;

    @OneToOne
    @JoinColumn(name = "channelEndAutoId", foreignKey = @ForeignKey(name = "FK_CHANNEL_ENV__CHANNEL_END_AUTO"), nullable = false, updatable = false)
    @Comment("상담시작 종료 PK")
    @NotNull
    private ChannelEndAuto end;


    @Enumerated(EnumType.STRING)
    @ColumnDefault("'category'")
    @Comment("고객 연결 방식")
    private SystemEnvEnum.CustomerConnection customerConnection;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'basic'")
    @Comment("상담직원 배정 방식")
    private SystemEnvEnum.MemberAssign memberAssign;

    @Convert(converter = BooleanConverter.class)
    @Column(length = 1)
    @ColumnDefault("'Y'")
    @Comment("상담직원 직접 연결 허용 Y : 사용 , N : 사용안함")
    private Boolean memberDirectEnabled;

    @Convert(converter = BooleanConverter.class)
    @Column(length = 1)
    @ColumnDefault("'N'")
    @Comment("상담 인입 제한")
    private Boolean requestBlockEnabled;

    @Comment("상담 불가 안내 메세지")
    @Column(length = 1000)
    @Convert(converter = IssuePayloadConverter.class)
    private IssuePayload impossibleMessage;

    /**
     * 배정 대기건수 제한
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "number", column = @Column(name = "assignStandbyNumber")),
            @AttributeOverride(name = "enabled", column = @Column(name = "assignStandbyEnabled")),
            @AttributeOverride(name = "message", column = @Column(name = "assignStandbyMessage", length = 1000))
    })
    private SystemIssuePayload.EnabledNumberMessage assignStandby;

    /**
     * 상담 평가요청
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "enabled", column = @Column(name = "evaluationEnabled", length = 1)),
            @AttributeOverride(name = "message", column = @Column(name = "evaluationMessage", length = 1000))
    })
    private SystemIssuePayload.EnabledMessage evaluation;

    @Comment("상담 배분 분류 최대 단계")
    @Range(min = 0, max = 3)
    @ColumnDefault("0")
    private Integer maxIssueCategoryDepth;

    @Comment("생성 회원 PK")
    private Long creator;

    @Comment("생성일")
    private ZonedDateTime created;


    @Comment("수정 회원 PK")
    @NotNull
    private Long modifier;

    @Comment("수정일")
    @NotNull
    private ZonedDateTime modified;
}
