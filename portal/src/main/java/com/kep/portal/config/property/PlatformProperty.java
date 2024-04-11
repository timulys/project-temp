package com.kep.portal.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * Platform 프로퍼티
 */
@ConfigurationProperties(prefix = "application.platform")
@Validated
@Data
public class PlatformProperty {

	@NotEmpty
	private String apiBasePath;
	@NotEmpty
	private String messagePath;
	@NotEmpty
	private String closePath;
	@NotEmpty
	private String relayPath;
}
