package com.kep.portal.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;


@ConfigurationProperties(prefix = "watcher")
@Validated
@Data
public class WatcherProperty {


	@NotEmpty
	private String apiBaseUrl;

	@NotEmpty
	private String groupKakaowork;

	private int groupId;
}