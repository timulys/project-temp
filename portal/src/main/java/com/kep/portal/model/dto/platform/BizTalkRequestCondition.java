package com.kep.portal.model.dto.platform;

import com.kep.core.model.dto.platform.BizTalkRequestStatus;
import com.kep.core.model.dto.platform.PlatformType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 톡 발송 요청 조회 검색 조건
 */
@Schema(description = "톡 발송 요청 조회 검색 조건")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BizTalkRequestCondition {

    @Schema(description = "시작일")
    private LocalDate startDate;
    @Schema(description = "종료일")
    private LocalDate endDate;

    @Schema(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template,\n" +
            "legacy_web, legacy_app , kakao_counsel_center)")
    private PlatformType type;

    @Schema(description = "비즈톡 요청 상태 목록 (ready: 대기\n" +
            "approve: 승인\n" +
            "reject: 반려\n" +
            "auto: 자동승인)")
    private List<BizTalkRequestStatus> status;

    @Schema(description = "브랜치 아이디")
    private Long branchId;
    @Schema(description = "팀 아이디")
    private Long teamId;
    @Schema(description = "사용자 아이디")
    private Long memberId;

}
