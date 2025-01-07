package com.dkt.always.talk.service;

import com.dkt.always.talk.config.property.Platform;
import com.dkt.always.talk.config.property.PlatformProperty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * BizTalk 관련 공통 서비스 클래스
 */
@Slf4j
public abstract class BizTalkCommonService {
    public String getTemplateRequestUrl(PlatformProperty platformProperty, @NotNull String endPoint, @NotNull String version) {
        Platform platform = platformProperty.getPlatforms().get("kakao_template");
        return makeUrl(endPoint, version, platform);
    }

    public String getAlimTalkRequesetUrl(PlatformProperty platformProperty, @NotNull String endPoint, @NotNull String version) {
        Platform platform = platformProperty.getPlatforms().get("kakao_alim_talk");
        return makeUrl(endPoint, version, platform);
    }

    private String makeUrl(String endPoint, String version, Platform platform) {
        Assert.notNull(platform, "PLATFORM IS NULL");
        return platform.getApiBaseUrl() + "/" + version + endPoint;
    }
}