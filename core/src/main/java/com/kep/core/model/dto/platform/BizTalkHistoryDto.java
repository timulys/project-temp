package com.kep.core.model.dto.platform;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.member.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BizTalkHistoryDto {
    @Schema(description = "비즈톡 히스토리 아이디")
    private Long id;


    @Schema(description = "플랫폼 타입 (solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template,\n" +
            "\tlegacy_web, legacy_app , kakao_counsel_center)")
    private PlatformType type;


    @Schema(description = "비즈톡 발송상태 (send : 발송중, succeed : 완료, failed : 실패)")
    private BizTalkSendStatus status;


    @JsonIncludeProperties({"name", "payload", "id"})
    @Schema(description = "플랫폼 템플릿 정보")
    private PlatformTemplateDto template;


    @Schema(description = "브랜치 아이디")
    private Long branchId;

    @Schema(description = "요청 아이디")
    private Long requestId;


    @Schema(description = "팀 아이디")
    private Long teamId;


    @Schema(description = "발송 일시")
    private ZonedDateTime sendDate;

    @Schema(description = "디테일")
    private String detail;

    @JsonIncludeProperties({"id", "name"})
    @Schema(description = "고객 정보")
    private CustomerDto customer;

    @Schema(description = "메시지 아이디")
    private String messageId;

    @Schema(description = "생성일시")
    private ZonedDateTime created;

    @JsonIncludeProperties({"id", "nickname"})
    @Schema(description = "생성자 정보")
    private MemberDto creator;

}
