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
public class KakaoBizSearchResponse {

    private String status;
    private String code;
    private String message;
    private String count;
    private String last_uid;
    private List<Result> results;
    private Boolean next;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result{
        private String uid;
        private String cid;
        private String reg_date;
        private String status_code;
        private String kko_status_code;
        private String sms_status_code;
        private String etc1;
        private String etc2;
        private String etc3;
        private String etc4;
        private String etc5;
        private String etc6;
        private String etc7;
        private String etc8;
        private String etc9;
        private String etc10;
        private String tax_cd1;
        private String tax_cd2;
    }
}
