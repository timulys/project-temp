package com.kep.portal.model.dto.platform;


import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.BizTalkTaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 톡 발송 예약 목록 검색 조건
 */
@Schema(description = "톡 발송 예약 목록 검색 조건")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BizTalkTaskCondition {

    @Schema(description = "시작일")
    private LocalDate startDate;
    @Schema(description = "종료일")
    private LocalDate endDate;
    @Schema(description = "날짜타입")
    private String dateType;

    @Schema(description = "플랫폼 타입 (solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template,\n" +
            "legacy_web, legacy_app , kakao_counsel_center)")
    private PlatformType platformType;

    @Schema(description = "비즈톡 예약상태 목록 (open, // 대기\n" +
            "cancel, // 취소\n" +
            "close, // 발송\n" +
            "error // 오류)")
    private List<BizTalkTaskStatus> status;

    @Schema(description = "브랜치 아이디")
    private Long branchId;
    @Schema(description = "팀 아이디")
    private Long teamId;
    @Schema(description = "사용자 아이디")
    private Long memberId;

    @Schema(description = "키워드")
    private String keyword;
    @Schema(description = "키워드 타입")
    private String keywordType;
}
