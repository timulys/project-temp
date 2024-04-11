package com.kep.portal.model.entity.platform;

import com.kep.core.model.dto.platform.BizTalkSendStatus;
import com.kep.core.model.dto.platform.PlatformType;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * 카카오 비즈톡, 발송 이력
 */
@Entity
@Table(indexes = {
        @Index(name = "IDX_BIZTALK_HISTORY__SEARCH", columnList = "branchId, type, status, teamId, sendDate, creator"),
        @Index(name = "IDX_BIZTALK_HISTORY__BRANCH_TEAM_MEMBER", columnList = "branchId , teamId , creator"),
        @Index(name = "IDX_BIZTALK_HISTORY__PLATFORM_TYPE", columnList = "type"),
        @Index(name = "IDX_BIZTALK_HISTORY__TEMPLATE", columnList = "templateId"),
        @Index(name = "IDX_BIZTALK_HISTORY__SEND_DATE", columnList = "sendDate"),
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BizTalkHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Comment("플랫폼 타입")
    private PlatformType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Comment("전송 상태")
    private BizTalkSendStatus status;

    @NotNull
    @Comment("브랜치 PK")
    private Long branchId;

    @Comment("요청 PK")
    @NotNull
    private Long requestId;

    @NotNull
    @Comment("그룹 PK")
    private Long teamId;

    @NotNull
    @Comment("고객 PK")
    private Long customerId;

    @Comment("템플릿 PK")
    private Long templateId;

    @Comment("메시지 ID")
    private String messageId;

    @NotNull
    @Comment("커스텀 PK")
    private String cid;

    @Comment("메시지 전송 날짜")
    private ZonedDateTime sendDate;

    @Comment("detail")
    @Lob
    @Basic
    @Nationalized
    private String detail;

    @NotNull
    @Comment("생성 일자")
    private ZonedDateTime created;

    @NotNull
    @Comment("생성자")
    private Long creator;

    @PrePersist
    public void prePersist() {
        if (created == null) {
            created = ZonedDateTime.now();
        }
    }

}
