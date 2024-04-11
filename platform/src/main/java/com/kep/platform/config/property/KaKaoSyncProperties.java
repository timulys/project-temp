package com.kep.platform.config.property;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

/**
 * Application 프로퍼티
 */
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.kakao-sync")
@Validated
@Data
public class KaKaoSyncProperties {
	
	@NotEmpty
	private String clientId;
	@NotEmpty
	private String redirectUri;
	
}
