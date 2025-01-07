package com.kep.core.model.dto.platform.kakao.bizTalk.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TalkSendResultResponseDto {
    private Integer seq_no;
    private String uid;
    private String cid;
    private String status_code;
    private String kko_status_code;
    private String kko_message;
    private String code;
    private String sms_status_code;
    private String ended_date;
    private String ended_yn;
}
