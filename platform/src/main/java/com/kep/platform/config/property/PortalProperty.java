package com.kep.platform.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * Portal 프로퍼티
 */
@ConfigurationProperties(prefix = "application.portal")
@Validated
@Data
public class PortalProperty {

	@NotEmpty
	private String apiBasePath;
	@NotEmpty
	private String openPath;
	@NotEmpty
	private String messagePath;
	@NotEmpty
	private String relayPath;
	@NotEmpty
	private String closePath;
	@NotEmpty
	private String callbackPath;
	@NotEmpty
	private String authorizedPath;
}
