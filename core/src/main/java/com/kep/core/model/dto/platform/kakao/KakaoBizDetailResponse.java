package com.kep.core.model.dto.platform.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoBizDetailResponse {
    private String code;
    private String message;
    private Result result;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result{
        private String uid;
        private String cid;
        private String reg_date;
        private String message_type;
        private String status_code;
        private String kko_status_code;
        private String sms_status_code;
        private String error_message;
        private String phone_number;
        private String template_code;
        private String req_sms_date;
        private String req_sms_yn;
        private String sender_no;
        private String sms_type;
        private String image_type;
        private String message;
        private String sms_message;
        private String title;
        private String kko_title;
        private String nat_cd;
        private String content_group_id;
        private String header;
    }
}
