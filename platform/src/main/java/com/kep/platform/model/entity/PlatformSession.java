package com.kep.platform.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kep.core.model.dto.event.PlatformEventType;
import com.kep.core.model.dto.issue.IssueType;
import com.kep.core.model.dto.platform.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

/**
 * 플랫폼 세션
 */
@RedisHash(value = "PlatformSession", timeToLive = 60 * 60) // 60 * 60 * 24 * 30
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformSession {

	public static final String ID_DELIM = "_";

	/**
	 * 식별자
	 */
	@Id
	private String id;

	/**
	 * 플랫폼 구분
	 */
	@NotNull
	private PlatformType platformType;

	/**
	 * 플랫폼 서비스 식별자 (카카오 상담톡 채널)
	 */
	@NotEmpty
	private String serviceKey;

	/**
	 * 플랫폼 고객 식별자
	 */
	@NotEmpty
	private String userKey;

	/**
	 * 이슈 식별자
	 */
	@NotNull
	@Positive
	private Long issueId;

	/**
	 * 생성 시간
	 */
	@NotNull
	private ZonedDateTime created;

	@JsonIgnore
	public static String buildId(@NotNull PlatformType platformType,
								 @NotEmpty String serviceKey, @NotEmpty String userKey) {

		return platformType + ID_DELIM
				+ serviceKey + ID_DELIM
				+ userKey;
	}
}
