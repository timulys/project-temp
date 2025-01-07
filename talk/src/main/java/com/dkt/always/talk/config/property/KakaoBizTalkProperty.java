package com.dkt.always.talk.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.kakao-bizmsg")
public class KakaoBizTalkProperty {
    private String clientId;
    private String clientSecret;
}
