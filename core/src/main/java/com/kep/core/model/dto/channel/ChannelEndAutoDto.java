package com.kep.core.model.dto.channel;

import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.core.model.dto.system.SystemIssuePayloadDto;
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

    @Positive
    private Long id;

    private SystemIssuePayloadDto.EnabledMessage register;
    private SystemIssuePayloadDto.EnabledNumberMessage memberDelay;
    private SystemIssuePayloadDto.EnabledNumberMessage guestDelay;
    private SystemIssuePayloadDto.EnabledNumberMessage guestNoticeDelay;
    private ChannelEndAutoDto.Guide guide;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Guide {

        private SystemEnvEnum.IssueEndType type;
        @PositiveOrZero
        private Integer number;
        private IssuePayload message;
        private IssuePayload noticeMessage;

    }
}
