package com.kep.core.model.dto.platform.kakao.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result{
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
    private String detail_code;
    private String detail_message;
}
