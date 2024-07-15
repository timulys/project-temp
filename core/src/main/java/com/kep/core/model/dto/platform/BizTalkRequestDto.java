package com.kep.core.model.dto.platform;

import com.fasterxml.jackson.annotation.*;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.customer.GuestDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BizTalkRequestDto {
    @Schema(description = "비즈톡 요청 아이디")
    private Long id;


    @Schema(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template,\n" +
            "legacy_web, legacy_app , kakao_counsel_center)")
    private PlatformType platform;


    @Schema(description = "비즈톡 요청 상태 (ready: 대기\n" +
            "approve: 승인\n" +
            "reject: 반려\n" +
            "auto: 자동승인)")
    private BizTalkRequestStatus status;


    @JsonIncludeProperties({"name","payload","status","id"})
    @Schema(description = "플랫폼 템플릿 정보")
    private PlatformTemplateDto template;

    @Schema(description = "채널 아이디")
    private Long channelId;


    @Schema(description = "브랜치 아이디")
    private Long branchId;


    @Schema(description = "팀 아이디")
    private Long teamId;


    @Schema(description = "수신일시")
    private ZonedDateTime reserved;


    /**
     * FIXME ::
     */
    @JsonIncludeProperties({"id","name"})
//    @Schema(description = "
//    private List<GuestDto> guests;
	@Schema(description = "고객 정보 목록 (?)")
    private List<CustomerDto> customers;

    @Schema(description = "비즈톡 요청 거절 사유")
    private String reasonReject;


    @Schema(description = "생성일시")
    private ZonedDateTime created;


    @JsonIncludeProperties({"id","nickname"})
    @Schema(description = "생성자 정보")
    private MemberDto creator;


    @Schema(description = "수정일시")
    private ZonedDateTime modified;


    @Schema(description = "수정자 정보")
    private Long modifier;


    // 전송 요청
    @Schema(description = "전송요청 대상 고객 목록")
    private List<Long> toCustomers;
    @Schema(description = "템플릿 아이디")
    private Long templateId;
    @Schema(description = "카카오 비즈톡 템플릿 정보")
    private KakaoBizMessageTemplatePayload friendPayload;
    @Schema(description = "수신일")
    private String reserveDate;
}
