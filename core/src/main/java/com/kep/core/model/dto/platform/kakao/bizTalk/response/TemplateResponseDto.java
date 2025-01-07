package com.kep.core.model.dto.platform.kakao.bizTalk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.channel.ChannelDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.ZonedDateTime;

/**
 * Template Response Dto
 */
@Schema(description = "플랫폼 템플릿 목록 정보")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateResponseDto extends ResponseDto {
    @Schema(description = "플랫폼 템플릿 아이디")
    private Long id;

    @Schema(description = "플랫폼 템플릿명")
    private String name;

    @Schema(description = "플랫폼 템플릿 상태(request, // 검수요청\n" +
            "approve, // 승인\n" +
            "reject, // 반려\n" +
            "temp, // 임시저장\n" +
            "delete // 삭제)")
    private PlatformTemplateStatus status;

    @Schema(description = "플랫폼 타입")
    private PlatformType platform;

    @JsonIncludeProperties({"id", "name", "service_id", "service_key"})
    @Schema(description = "채널 정보")
    private ChannelDto channelInfo;

    @Schema(description = "카카오 비즈톡 템플릿 포맷")
    private KakaoBizMessageTemplatePayload detail;

    @Schema(description = "브랜치 아이디")
    private Long branchId;

    @Schema(description = "생성일시")
    private ZonedDateTime created;

    @JsonIncludeProperties({"id", "username", "nickname", "teams"})
    @Schema(description = "생성자 정보")
    private MemberDto creatorInfo;

    @Schema(description = "수정일시")
    private ZonedDateTime modified;

    @JsonIncludeProperties({"id", "username", "nickname", "teams"})
    @Schema(description = "수정자 정보")
    private MemberDto modifierInfo;

    @Schema(description = "거절여부")
    private Boolean rejectYn;

    @Schema(description = "거절사유")
    private String rejectComment;


    public void updateCreator(MemberDto creatorDto) {
        this.creatorInfo = creatorDto;
    }

    public void updateModifier(MemberDto modifierDto) {
        this.modifierInfo = modifierDto;
    }

    public void updateDetail(KakaoBizMessageTemplatePayload kakaoBizMessageTemplatePayload) {
        this.detail = kakaoBizMessageTemplatePayload;
    }

    public void updateChannel(ChannelDto channelDto) {
        this.channelInfo = channelDto;
    }
}
