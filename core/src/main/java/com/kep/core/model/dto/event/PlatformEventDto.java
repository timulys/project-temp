package com.kep.core.model.dto.event;

import com.kep.core.model.dto.platform.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * 정규화된 플랫폼 이벤트
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformEventDto {

	/**
	 * 플랫폼 식별
	 */
	@NotNull
	private PlatformType platformType;

	/**
	 * 플랫폼 이벤트 식별자
	 */
	private String eventKey;

	/**
	 * 플랫폼 이벤트 타입
	 */
	@NotNull
	private PlatformEventType platformEventType;

	/**
	 * 플랫폼 서비스 식별 (카카오 상담톡 채널)
	 */
	@NotEmpty
	private String serviceKey;

	/**
	 * 플랫폼 고객 식별
	 */
	@NotEmpty
	private String userKey;

	/**
	 * 파라미터
	 */
	private Map<String, Object> params;

	/**
	 * Payload
	 */
	private String payload;

	/**
	 * 생성 시간
	 */
	@NotNull
	private ZonedDateTime created;

	/**
	 * 처리 횟수
	 */
	@Builder.Default
	@NotNull
	private Integer consumeCount = 1;

	/**
	 * 이벤트 식별자, 로그 추적용
	 */
	public Long getTrackKey() {

		if (this.created == null) {
			this.created = ZonedDateTime.now();
		}

		return this.created.toInstant().toEpochMilli();
	}
}
