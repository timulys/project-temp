package com.kep.core.model.dto.platform.kakao.bizTalk.request.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.platform.kakao.KakaoAlertSendEvent;
import com.kep.core.model.dto.platform.kakao.KakaoFriendSendEvent;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Button {
    // 봇 연결 이벤트명
    private String chatEvent;
    // 봇 전환 메타 정보
    private String chatExtra;
    // 버튼명
    @NotEmpty
    private String name;

    @NotEmpty
    private ButtonType type;

    private String schemeAndroid;

    private String schemeIos;

    private String urlMobile;

    private String urlPc;

    private String pluginId;

    private String relayId;

    private String oneclickId;

    private String productId;

    private String target;

    public static Button ofAlimTalk(KakaoAlertSendEvent.Message.Button button) {
        return Button.builder()
                .chatEvent(button.getChatEvent())
                .chatExtra(button.getChatExtra())
                .name(button.getName())
                .type(ButtonType.valueOf(button.getType().toString()))
                .schemeAndroid(button.getSchemeAndroid())
                .schemeIos(button.getSchemeIos())
                .urlMobile(button.getUrlMobile())
                .urlPc(button.getUrlPc())
                .pluginId(button.getPluginId())
                .relayId(button.getRelayId())
                .oneclickId(button.getOneclickId())
                .productId(button.getProductId())
                .target(button.getTarget())
                .build();
    }

    public static Button ofFriendTalk(KakaoFriendSendEvent.Message.Button button) {
        return Button.builder()
                .name(button.getName())
                .type(ButtonType.valueOf(button.getType().toString()))
                .schemeAndroid(button.getSchemeAndroid())
                .schemeIos(button.getSchemeIos())
                .urlMobile(button.getUrlMobile())
                .urlPc(button.getUrlPc())
                .build();
    }
}
