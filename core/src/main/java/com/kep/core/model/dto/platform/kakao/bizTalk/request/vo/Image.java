package com.kep.core.model.dto.platform.kakao.bizTalk.request.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {
    // 이미지 클릭시 이동할 URL
    private String imgLink;
    // 노출할 이미지
    @NotEmpty
    private String imgUrl;
}
