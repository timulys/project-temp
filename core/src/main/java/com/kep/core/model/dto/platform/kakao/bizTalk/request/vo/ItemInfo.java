package com.kep.core.model.dto.platform.kakao.bizTalk.request.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemInfo {
    // 타이틀
    private String title;
    // 부가정보
    private String description;
}
