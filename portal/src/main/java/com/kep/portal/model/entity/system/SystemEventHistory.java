package com.kep.portal.model.entity.system;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.portal.model.entity.member.Member;
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
@Table(indexes = { @Index(name = "IDX_SYSTEM_EVENT_HISTORY__ENTITY", columnList = "entity")
        , @Index(name = "IDX_SYSTEM_EVENT_HISTORY__BRANCH_TEAM", columnList = "branchId,teamId")
        , @Index(name = "IDX_SYSTEM_EVENT_HISTORY__TO_MEMBER", columnList = "toMemberId")
        , @Index(name = "IDX_SYSTEM_EVENT_HISTORY__ACTION", columnList = "action")
        , @Index(name = "IDX_SYSTEM_EVENT_HISTORY__DATE", columnList = "created")
})
public class SystemEventHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;


    @NotNull
    @Comment("BRANCH PK")
    private Long branchId;

    @Comment("TEAM PK")
    private Long teamId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fromMemberId", foreignKey = @ForeignKey(name = "FK_SYSTEM_EVENT_HISTORY__FROM_MEMBER") , nullable = false, updatable = false)
    @Comment("member PK")
    @JsonIncludeProperties({"id","username","nickname"})
    private Member fromMember;


    @NotNull
    @Column(nullable = false, updatable = false)
    @Comment("member PK")
    @JsonIncludeProperties({"id","username","nickname"})
    private Long toMemberId;

    @NotNull
    @Comment("행동")
    @Enumerated(EnumType.STRING)
    private SystemEventHistoryActionType action;

    @NotNull
    @Comment("entity name")
    private String entity;

    @Column(length = 4000)
    @Comment("before")
    private String beforPayload;


    @Column(length = 4000)
    @Comment("after")
    private String afterPayload;

    @Comment("client ip")
    private String clientIp;

    @Comment("login user-agent")
    private String userAgent;

    @Comment("CREATE : 생성 , UPDATE : 수정 , DELETE : 삭제")
    private String cud;

    @Comment("생성 일시")
    @NotNull
    private ZonedDateTime created;

}
