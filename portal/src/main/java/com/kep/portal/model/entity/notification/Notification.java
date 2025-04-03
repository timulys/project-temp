package com.kep.portal.model.entity.notification;

import com.kep.portal.model.dto.notification.*;
import com.kep.portal.model.entity.member.Member;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * 상담원 알림
 * {@link NotificationType} 상담원 알림 종류
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Positive
    @Comment("PK")
    private Long id;

    /**
     * 알림받을 멤버
     */
    @NotNull
    @Comment("알림 받은 멤버 PK")
    private Long memberId;

    /**
     * 타이틀
     * 상담직원 전환이 실패되었습니다.
     * 상담내용 검토가 요청되었습니다.
     * ...
     */
    @NotEmpty
    @Size(max = 100)
    @Comment("타이틀 ex)상담내용 검토가 요청되었습니다.")
    private String title;

    /**
     * 내용(이유)
     */
    @NotEmpty
    @Size(max = 500)
    @Comment("내용")
    private String payload;

    /**
     * 알림 종류
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Comment("종류")
    private NotificationType type;

    /**
     * TOAST, ALERT, CONFIRM ...
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Comment("디스플레이 타입")
    private NotificationDisplayType displayType;

    /**
     * 읽음 처리
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Comment("읽음 유무")
    private NotificationStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Comment("아이콘 종류")
    private NotificationIcon icon;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Comment("타겟")
    private NotificationTarget target;

    @Comment("알림 이미지 URL")
    @URL
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("생성자")
    private Member creator;
    @NotNull
    @Comment("생성일")
    private ZonedDateTime created;

    @PrePersist
    public void prePersist() {
        if (created == null) {
            created = ZonedDateTime.now();
        }
    }
}
