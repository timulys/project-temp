package com.kep.core.model.dto.platform.kakao.bizTalk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.ResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TalkSendResponseDto extends ResponseDto {
    private String uid;
    private String cid;
    private String code;
    private String image;
    private String message;
    private String report_group_no;
    private List<TalkSendResultResponseDto> results;
}
