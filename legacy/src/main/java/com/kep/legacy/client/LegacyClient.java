package com.kep.legacy.client;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Map;

/**
 * 기간계 호출
 */
@Component
public interface LegacyClient {

	Map<String, Object> request(@NotNull String path,
			@NotNull Map<String, Object> requestBody,
			@Positive Long trackKey) throws Exception;
}
