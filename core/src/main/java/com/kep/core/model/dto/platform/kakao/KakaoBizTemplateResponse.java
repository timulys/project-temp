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
public class KakaoBizTemplateResponse<T> {

    private String code;
    private String message;
    private Integer totalCount;
    private String image;
    private T data;



    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateCategory {
        private String code;
        private String name;
        private String groupName;
        private String inclusion;
        private String exclusion;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileCategory {
        private String code;
        private String name;
    }
}
