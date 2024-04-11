package com.kep.platform.config.security;

import com.kep.platform.config.property.PlatformProperty;
import com.kep.platform.model.security.ApiKeyAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * API Key 인증 토큰 생성 ({@link ApiKeyAuthenticationToken})
 * {@link ApiKeyAuthenticationProvider} 에서 인증 진행
 */
@Slf4j
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

	private final PlatformProperty platformProperty;

	public ApiKeyAuthenticationFilter(PlatformProperty platformProperty) {
		this.platformProperty = platformProperty;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		// TODO: API Key 필요한 end point
		if (request.getRequestURI().contains("/actuator/")) {
			String apiKey = getApiKey(request);
			log.info("API KEY FILTER, {}", apiKey);
			ApiKeyAuthenticationToken authToken = new ApiKeyAuthenticationToken(apiKey, AuthorityUtils.NO_AUTHORITIES);
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}

		filterChain.doFilter(request, response);
	}

	@Nullable
	private String getApiKey(@NotNull HttpServletRequest httpRequest) {

		String authValue = httpRequest.getHeader(platformProperty.getApiKeyName());
		log.debug("authValue: {}", authValue);
		if (!ObjectUtils.isEmpty(authValue)) {
			return authValue.trim();
		}

		return null;
	}
}
