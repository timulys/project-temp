package com.kep.platform.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Application 프로퍼티
 */
@ConfigurationProperties(prefix = "application.core")
@Validated
@Data
public class CoreProperty {

	@NotEmpty
	private String portalServiceUri;
	@NotEmpty
	private String portalDomain;

	private ProxyServer proxyServer;

	public static final String kakaoCounselTalkBaseUrl = "https://bizmessage.kakao.com/chat";

	/**
	 * 프록시 정보
	 */
	@Validated
	@Data
	public static class ProxyServer {

		@NotEmpty
		private String host;
		@NotNull
		private Integer port;
		@NotEmpty
		private String user;
		@NotEmpty
		private String passwd;
	}
}
