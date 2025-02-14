package com.kep.core.model.dto.platform.kakao.bizTalk.request.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuickReply {
    @NotEmpty
    private String name;

    @NotEmpty
    private QuickReplyType type;

    private String schemeAndroid;

    private String schemeIos;

    private String urlMobile;

    private String urlPc;

    private String chatExtra;

    private String chatEvent;

    private String target;

}
