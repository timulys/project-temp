package com.kep.core.model.dto.platform.kakao.bizTalk.request.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemHighlight {
    // 타이틀(이미지가 있는 경우 최대 21자)
    @NotEmpty
    @Size(max = 30)
    private String title;
    // 부가정보(이미지가 잇는 경우 최대 13자)
    @NotEmpty
    @Size(max = 19)
    private String description;
}
