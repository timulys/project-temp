package com.kep.core.model.dto.platform.kakao;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    /* V2와 V3를 모두 대응하기 위해 Alias 추가 */
    @JsonAlias(value = {"list", "data"})
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
