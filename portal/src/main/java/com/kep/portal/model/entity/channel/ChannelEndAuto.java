package com.kep.portal.model.entity.channel;


import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.portal.model.converter.IssuePayloadConverter;
import com.kep.portal.model.entity.system.SystemIssuePayload;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChannelEndAuto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Positive
    private Long id;

    /**
     * 근무 외 시간 상담 접수 사용
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "enabled", column = @Column(name = "registerEnabled",length = 1)),
            @AttributeOverride(name = "message", column = @Column(name = "registerMessage",length = 1000))
    })
    private SystemIssuePayload.EnabledMessage register;

    /**
     * 상담대기 중 상담직원응답 지연 안내
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "enabled", column = @Column(name = "memberDelayEnabled",length = 1)),
            @AttributeOverride(name = "message", column = @Column(name = "memberDelayMessage",length = 1000)),
            @AttributeOverride(name = "number", column = @Column(name = "memberDelayMinute")),
    })
    private SystemIssuePayload.EnabledNumberMessage memberDelay;

    /**
     * 고객응답 지연 자동종료 사용
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "enabled", column = @Column(name = "guestDelayEnabled",length = 1)),
            @AttributeOverride(name = "message", column = @Column(name = "guestDelayMessage",length = 1000)),
            @AttributeOverride(name = "number", column = @Column(name = "guestDelayMinute")),
    })
    private SystemIssuePayload.EnabledNumberMessage guestDelay;


    /**
     * 고객응답 지연 자동종료 예고 사용
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "enabled", column = @Column(name = "guestNoticeDelayEnabled",length = 1)),
            @AttributeOverride(name = "message", column = @Column(name = "guestNoticeDelayMessage",length = 1000)),
            @AttributeOverride(name = "number", column = @Column(name = "guestNoticeDelayeMinute")),
    })
    private SystemIssuePayload.EnabledNumberMessage guestNoticeDelay;

    /**
     * 상담종료 안내
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "guideType")),
            @AttributeOverride(name = "number", column = @Column(name = "guideNumber")),
            @AttributeOverride(name = "message", column = @Column(name = "guideMessage",length = 1000)),
            @AttributeOverride(name = "noticeMessage", column = @Column(name = "guideNoticeMessage",length = 1000))
    })
    private ChannelEndAuto.Guide guide;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class Guide {

        @Enumerated(EnumType.STRING)
        @NotNull
        private SystemEnvEnum.IssueEndType type;

        @PositiveOrZero
        @ColumnDefault("5")
        private Integer number;

        @Convert(converter = IssuePayloadConverter.class)
        @Column(length = 4000)
        private IssuePayload message;

        @Convert(converter = IssuePayloadConverter.class)
        @Column(length = 4000)
        private IssuePayload noticeMessage;

    }
}
