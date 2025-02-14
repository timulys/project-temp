package com.kep.core.model.dto.platform.kakao.bizTalk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TalkSendResultResponseDto {
    private String detail_code;
    private String detail_message;
}
