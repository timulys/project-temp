package com.kep.platform.config.property;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Platform 모듈 프로퍼티
 */
@ConfigurationProperties(prefix = "application.platform")
@Validated
@Data
public class PlatformProperty {

	@NotEmpty
	private String apiVersion;
	@NotEmpty
	private String apiBasePath;
	@NotEmpty
	private String apiKeyName;
	//	@NotEmpty
	private String apiKeyMethod;
	@NotNull
	private boolean allowReceiveImage;
	@NotNull
	private boolean allowReceiveFile;
	@NotEmpty
	private String replaceMessageForPrevent;

	@NotEmpty
	private Map<String, Platform> platforms;
	@NotEmpty
	private List<Client> clients;
	private List<String> whitelist;

	private String redisMode;

	@Validated
	@Data
	public static class Platform {
		@NotEmpty
		@URL
		private String apiBaseUrl;
		private String apiKey;
	}

	/**
	 * API 호출하는 클라이언트 정보
	 */
	@Validated
	@Data
	public static class Client {

		@NotEmpty
		private String id;
		@NotEmpty
		private String name;
		@NotEmpty
		private String apiKey;
		//		@NotEmpty
		private List<String> roles;
	}
}