package com.kep.core.model.dto.platform.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoBizTalkSendResponse {
    private String uid;

    private String cid;

    private String code;

    private String image;

    private String message;

    private String report_group_no;

    private List<Result> results;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result{
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
}
