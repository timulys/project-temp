package com.kep.portal.model.dto.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.portal.model.entity.notification.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Schema(description = "노티 아이디")
    private Long id;

    /**
     * 알림받을 멤버
     */
    @NotNull
    @Schema(description = "노티 수신 사용자 아이디")
    private Long memberId;

    /**
     * 타이틀
     * 상담직원 전환이 실패되었습니다.
     * 상담내용 검토가 요청되었습니다.
     * ...
     */
    @NotEmpty
    @Size(max = 100)
    @Schema(description = "제목")
    private String title;

    /**
     * 내용(이유)
     */
    @Size(max = 500)
    @Schema(description = "내용 (이유)")
    private String payload;

    /**
     * 알림 종류
     */
    @NotNull
    @Schema(description = "알림 타입 (delay_issue_room : 상담방 지연\n" +
            "done_member_assignment : 상담직원 배정 완료\n" +
            "request_member_transform : 상담직원 전환 요청\n" +
            "done_member_transform : 상담직원 전환 완료\n" +
            "refer_member_transform : 상담직원 전환 반려\n" +
            "fail_manual_member_transform : 상담직원 전환 실패\n" +
            "fail_auto_member_transform : 상담직원 전환 실패\n" +
            "request_review_counselling_details : 상담내용 검토 요청\n" +
            "done_review_counselling_details : 상담내용 검토 완료\n" +
            "refer_review_counselling_details : 상담내용 검토 반려\n" +
            "done_consultation_transfer : 상담 이관 완료\n" +
            "fail_consultation_transfer : 상담 이어받기 실패\n" +
            "end_counsel : 상담 종료\n" +
            "notice : 공지사항\n" +
            "talk_request_approve : 톡 발송 승인 요청\n" +
            "talk_approve : 톡 요청 승인\n" +
            "talk_reject : 톡 요청 반려\n" +
            "expired_by_duplication : 중복으로 인한 세션 만료)")
    private NotificationType type;

    @Schema(description = "알림 타입 한국어 ( key , value 형태로 type에 정의된 값 )")
    private String korType;

    /**
     * TOAST, ALERT, CONFIRM ...
     */
    @NotNull
    @Schema(description = "표시 타입 (toast, alert, confirm)")
    private NotificationDisplayType displayType;
    /**
     * 읽음 처리
     */
    @NotNull
    @Schema(description = "노티 상태(read, unread)")
    private NotificationStatus status;

    @NotNull
    @Schema(description = "노티 아이콘 타입(member, system, none)")
    private NotificationIcon icon;

    @NotNull
    @Schema(description = "노티 대상 유형 (branch\n" +
            "member\n" +
            "team\n" +
            "admin\n" +
            "manager\n" +
            "manager_admin\n" +
            "all)")
    private NotificationTarget target;


    @Schema(description = "url")
    private String url;

    @JsonIncludeProperties({"id","username","nickname","profile"})
    @Schema(description = "생성자")
    private MemberDto creator;

    @NotNull
    @Schema(description = "생성일시")
    private ZonedDateTime created;

    public static NotificationDto from(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setMemberId(notification.getMemberId());
        dto.setTitle(notification.getTitle());
        dto.setPayload(notification.getPayload());
        dto.setType(notification.getType());
        dto.setDisplayType(notification.getDisplayType());
        dto.setStatus(notification.getStatus());
        dto.setIcon(notification.getIcon());
        dto.setTarget(notification.getTarget());
        dto.setUrl(notification.getUrl());
        dto.setMemberId(notification.getCreator() != null ? notification.getCreator().getId() : null);
        dto.setCreated(notification.getCreated());

        return dto;
    }
}
