package com.kep.portal.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@ConfigurationProperties(prefix = "application.core")
@Validated
@Data
public class CoreProperty {

	@NotEmpty
	private String platformServiceUri;
	@NotEmpty
	private String legacyServiceUri;
//	@NotEmpty
	private List<Server> platformServers;
	
	private String syncClientId;

	@Validated
	@Data
	public static class Server {

		@NotEmpty
		private String id;
		@NotEmpty
		private String host;
		@NotNull
		@Positive
		private Integer port;
	}
}
