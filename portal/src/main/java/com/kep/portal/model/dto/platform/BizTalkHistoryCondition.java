package com.kep.portal.model.dto.platform;

import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.BizTalkSendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 톡 발송 이력 조회 검색 조건
 */

@Schema(description = "톡 발송 이력 조회 검색조건")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BizTalkHistoryCondition {

    @Schema(description = "시작일")
    private LocalDate startDate;
    @Schema(description = "종료일")
    private LocalDate endDate;

    @Schema(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template,\n" +
            "\tlegacy_web, legacy_app , kakao_counsel_center)")
    private PlatformType type;

    @Schema(description = "발송 상태 목록(send : 발송중\n" +
            "succeed : 발송완료\n" +
            "failed : 실패)")
    private List<BizTalkSendStatus> status;

    @Schema(description = "브랜치 아이디")
    private Long branchId;
    @Schema(description = "팀 아이디")
    private Long teamId;
    @Schema(description = "사용자 아이디")
    private Long memberId;

    @Schema(description = "키워드")
    private String keyword;

    //FIXME :: 명시적이지 않음. 타입 찾아야함 20240715 volka
    @Schema(description = "키워드 타입 (?)")
    private String keywordType;

}
