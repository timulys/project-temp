package com.kep.platform.config.security;

import com.kep.platform.config.property.PlatformProperty;
import com.kep.platform.model.security.ApiKeyAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@link ApiKeyAuthenticationFilter} 에서 필터링된 요청 인증 진행
 */
@Component
@Slf4j
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

	@Resource
	private PlatformProperty platformProperty;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		ApiKeyAuthenticationToken authToken = (ApiKeyAuthenticationToken) authentication;
		String authKey = authToken.getKey();
		log.info("API KEY AUTH PROVIDER, KEY: {}", authKey);

		Map<String, PlatformProperty.Client> clients = platformProperty.getClients().stream()
				.collect(Collectors.toMap(PlatformProperty.Client::getApiKey, Function.identity()));

		List<GrantedAuthority> authorities = new ArrayList<>();
		if (!ObjectUtils.isEmpty(authKey)) {
			PlatformProperty.Client client = clients.get(authKey);
			if (client != null) {
				for (String role : client.getRoles()) {
					authorities.add(() -> "ROLE_" + role);
				}
			} else {
				throw new BadCredentialsException("Invalid API Key");
			}
		} else {
			throw new BadCredentialsException("No API Key");
		}

		log.info("ROLES: {}", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
		authToken = new ApiKeyAuthenticationToken(authKey, authorities);
		authToken.setAuthenticated(true);
		return authToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {

		return (ApiKeyAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
