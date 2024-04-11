package com.kep.legacy.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * 기간계 프로퍼티
 */
@ConfigurationProperties(prefix = "application.portal")
@Validated
@Data
public class PortalProperty {

	@NotEmpty
	private String apiBasePath;
}
