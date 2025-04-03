package com.kep.portal.model.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    delay_issue_room("상담방 지연"),
    done_member_assignment("상담직원 배정 완료"),
    request_member_transform("상담직원 전환 요청"),
    done_member_transform("상담직원 전환 완료"),
    done_member_transform_auto("상담직원 전환 완료"),
    refer_member_transform("상담직원 전환 반려"),
    fail_manual_member_transform("상담직원 전환 실패"),
    fail_auto_member_transform("상담직원 전환 실패"),

    request_review_counselling_details("상담내용 검토 요청"),
    done_review_counselling_details("상담내용 검토 완료"),
    refer_review_counselling_details("상담내용 검토 반려"),

    done_consultation_transfer("상담 이관 완료"),
    fail_consultation_transfer("상담 이어받기 실패"),

    end_counsel("상담 종료"),

    notice("공지사항"),

    talk_request_approve("톡 발송 승인 요청"),
    talk_approve("톡 요청 승인"),
    talk_reject("톡 요청 반려"),
    expired_by_duplication("중복으로 인한 세션 만료");

    private String kor;

}
