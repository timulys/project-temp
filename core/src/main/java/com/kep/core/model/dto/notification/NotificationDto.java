package com.kep.core.model.dto.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NotificationDto {

    @Positive
    private Long id;

    /**
     * 알림받을 멤버
     */
    @NotNull
    private Long memberId;

    /**
     * 타이틀
     * 상담직원 전환이 실패되었습니다.
     * 상담내용 검토가 요청되었습니다.
     * ...
     */
    @NotEmpty
    @Size(max = 100)
    private String title;

    /**
     * 내용(이유)
     */
    @Size(max = 500)
    private String payload;

    /**
     * 알림 종류
     */
    @NotNull
    private NotificationType type;

    private String korType;

    /**
     * TOAST, ALERT, CONFIRM ...
     */
    @NotNull
    private NotificationDisplayType displayType;
    /**
     * 읽음 처리
     */
    @NotNull
    private NotificationStatus status;

    @NotNull
    private NotificationIcon icon;

    @NotNull
    private NotificationTarget target;


    private String url;

    @JsonIncludeProperties({"id","username","nickname","profile"})
    private MemberDto creator;

    @NotNull
    private ZonedDateTime created;
}
