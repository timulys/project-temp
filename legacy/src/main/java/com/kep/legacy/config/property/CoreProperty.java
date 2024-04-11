package com.kep.legacy.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 기본 프로퍼티
 */
@ConfigurationProperties(prefix = "application.core")
@Validated
@Data
public class CoreProperty {

	@NotEmpty
	private String portalServiceUri;

	private List<String> whitelist;
}
