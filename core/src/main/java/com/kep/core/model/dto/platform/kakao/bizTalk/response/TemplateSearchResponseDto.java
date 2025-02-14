package com.kep.core.model.dto.platform.kakao.bizTalk.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.platform.BizTalkMessageType;
import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.PlatformType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Schema(description = "템플릿 목록 조회용 DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateSearchResponseDto extends ResponseDto {
    @Schema(description = "템플릿 아이디")
    private Long id;
    @Schema(description = "템플릿 이름")
    private String name;
    @Schema(description = "템플릿 코드")
    private String code;
    @Schema(description = "템플릿 상태")
    private PlatformTemplateStatus status;
    @Schema(description = "템플릿 메시지 타입")
    private BizTalkMessageType messageType;
    @Schema(description = "템플릿 채널 아이디")
    private Long channelId;
    @Schema(description = "템플릿 채널 발신 프로필 키")
    private String senderProfileKey;
    @Schema(description = "템플릿 내용")
    private String payload;
    @Schema(description = "템플릿 사용 플랫폼 타입")
    private PlatformType platform;
    @Schema(description = "브랜치 아이디")
    private Long branchId;
    @Schema(description = "템플릿 생성일시")
    private ZonedDateTime created;
    @JsonIgnore
    @Schema(description = "템플릿 생성자")
    private MemberDto creator;
    @Schema(description = "템플릿 수정일시")
    private ZonedDateTime modified;
    @JsonIgnore
    @Schema(description = "템플릿 수정자")
    private MemberDto modifier;

    public void updateCreator(MemberDto creatorDto) {
        this.creator = creatorDto;
    }

    public void updateModifier(MemberDto modifierDto) {
        this.modifier = modifierDto;
    }
}
