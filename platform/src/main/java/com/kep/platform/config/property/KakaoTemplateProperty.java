package com.kep.platform.config.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "security.oauth2.client.registration.kakao-template")
public class KakaoTemplateProperty {
    private String clientId;
    private String clientSecret;
}
