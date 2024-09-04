package com.kep.portal.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalTime;
import java.util.Set;

@ConfigurationProperties(prefix = "application.portal")
@Validated
@Data
public class PortalProperty {

	@NotEmpty
	private String apiVersion;
	@NotEmpty
	private String apiBasePath;
	@NotEmpty
	private String serviceUrl;
	@NotEmpty
	private String serviceDomain;

	@NotEmpty
	private String storagePath;
	@NotEmpty
	private String uploadPath;
	@NotEmpty
	private String uploadUrl;

	@NotEmpty
	private String downloadApiUrl;

	@NotEmpty
	private String evaluationLinkUrl;

	private Set<String> allocateExtension;

	//기존 로직
	public static final String kakaoCounselTalkBaseUrl = "https://bizmessage.kakao.com/chat";

	@NotNull
	@Positive
	private Long systemMemberId;
	@NotNull
	@Positive
	private Long kakaoBotMemberId;

	private String redisMode;

	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime breakTimeStart;

	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime breakTimeEnd;

}
