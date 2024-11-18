package com.kep.portal.config.property;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@ConfigurationProperties(prefix = "application.socket")
@Validated
@Data
public class SocketProperty {
	@NotEmpty
	private String endpoint;
	@NotEmpty
	private String generalPath;
	@NotEmpty
	private String directPath;
	@NotEmpty
	private String branchPath;
	@NotEmpty
	private String issuePath;
	@NotEmpty
	private String managerPath;
	@NotEmpty
	private String adminPath;
	@NotEmpty
	private String managerAdminPath;
	@NotEmpty
	private String noticePath;
	@NotNull
	private MessageBroker messageBroker;

	@Validated
	@Data
	public static class MessageBroker {

		private List<String> addresses;
		@NotEmpty
		private String host;
		@NotNull
		@Positive
		private Integer port;
		@NotEmpty
		private String virtualHost;
		@NotEmpty
		private String username;
		@NotEmpty
		private String password;
		@NotNull
		@Positive
		private Integer heartbeatInterval;
	}
}
