package com.kep.portal.model.entity.channel;

import com.kep.portal.model.entity.system.SystemIssuePayload;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChannelStartAuto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;

    /**
     * 세션 시작 메시지
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "enabled", column = @Column(name = "stEnabled",length = 1)),
            @AttributeOverride(name = "code", column = @Column(name = "stCode")),
            @AttributeOverride(name = "message", column = @Column(name = "stMessage",length = 1000))
    })
    @Comment("세션 시작 메시지")
    private SystemIssuePayload.EnabledCodeMessage st;

    /**
     * 배정대기(상담접수) 안내 [S1 상담불가]
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "enabled", column = @Column(name = "unableEnabled",length = 1)),
            @AttributeOverride(name = "code", column = @Column(name = "unableCode")),
            @AttributeOverride(name = "message", column = @Column(name = "unableMessage",length = 1000))
    })
    private SystemIssuePayload.EnabledCodeMessage unable;

    /**
     * 배정대기/상담대기 안내 [S2 상담부재]
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "enabled", column = @Column(name = "absenceEnabled",length = 1)),
            @AttributeOverride(name = "code", column = @Column(name = "absenceCode")),
            @AttributeOverride(name = "message", column = @Column(name = "absenceMessage",length = 1000))
    })
    private SystemIssuePayload.EnabledCodeMessage absence;

    /**
     * 상담대기 안내 [S4 상담대기]
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "enabled", column = @Column(name = "waitingEnabled",length = 1)),
            @AttributeOverride(name = "code", column = @Column(name = "waitingCode")),
            @AttributeOverride(name = "message", column = @Column(name = "waitingMessage",length = 1000))
    })
    private SystemIssuePayload.EnabledCodeMessage waiting;

    /**
     * 상담시작 공통 인사말
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "enabled", column = @Column(name = "welcomEnabled",length = 1)),
            @AttributeOverride(name = "message", column = @Column(name = "welcomMessage",length = 1000)),
    })
    private SystemIssuePayload.EnabledMessage welcom;
}
