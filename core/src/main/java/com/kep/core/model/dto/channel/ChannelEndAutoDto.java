package com.kep.core.model.dto.channel;

import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.core.model.dto.system.SystemIssuePayloadDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelEndAutoDto {

    @Schema(description = "상담 종료 설정 아이디 (PK)")
    @Positive
    private Long id;
    @Schema(name = "register" , description = "근무 외 시간 상담 접수 사용 여부 설정" , implementation = SystemIssuePayloadDto.EnabledNumberMessage.class)
    private SystemIssuePayloadDto.EnabledMessage register;
    @Schema(name = "memberDelay" , description = "상담대기 중 상담직원응답 지연 안내 설정" , implementation = SystemIssuePayloadDto.EnabledNumberMessage.class)
    private SystemIssuePayloadDto.EnabledNumberMessage memberDelay;
    @Schema(name = "guestDelay" , description = "고객응답 지연 자동종료 설정" , implementation = SystemIssuePayloadDto.EnabledNumberMessage.class)
    private SystemIssuePayloadDto.EnabledNumberMessage guestDelay;
    @Schema(name = "guestNoticeDelay" , description = "고객응답 지연 자동종료 예고 설정" , implementation = SystemIssuePayloadDto.EnabledNumberMessage.class)
    private SystemIssuePayloadDto.EnabledNumberMessage guestNoticeDelay;
    @Schema(description = "상담종료 안내 설정")
    private ChannelEndAutoDto.Guide guide;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Guide {
        @Schema(description = "상담종료 안내 설정 타입 ( notice : 종료 예고 사용  , instant : 즉시 종료 )")
        private SystemEnvEnum.IssueEndType type;
        @Schema(description = "상담종료 예고 후 자동종료 설정 시간")
        @PositiveOrZero
        private Integer number;
        @Schema(name = "message" , description = "상담종료 안내 메세지" , implementation = IssuePayload.class)
        private IssuePayload message;
        @Schema(name = "noticeMessage" , description = "상담종료 예고 후 자동종료 메세지" , implementation = IssuePayload.class)
        private IssuePayload noticeMessage;
    }
}
