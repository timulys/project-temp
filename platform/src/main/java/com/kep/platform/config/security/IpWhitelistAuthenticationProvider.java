package com.kep.platform.config.security;

import com.kep.platform.config.property.PlatformProperty;
import com.kep.platform.model.security.IpWhitelistAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link IpWhitelistAuthenticationFilter} 에서 필터링된 요청 인증 진행
 */
@Component
@Slf4j
public class IpWhitelistAuthenticationProvider implements AuthenticationProvider {

	@Resource
	private PlatformProperty platformProperty;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		IpWhitelistAuthenticationToken authToken = (IpWhitelistAuthenticationToken) authentication;
		String authKey = authToken.getKey();
		log.info("IP WHITELIST AUTH PROVIDER, KEY: {}", authKey);

		// ROLE by Ip
		List<String> whitelist = platformProperty.getWhitelist();

		// ROLE_SYS
		// TODO: subnet ip pattern
		if (!whitelist.contains(authKey)) {
			log.info("Invalid IP Address");
			throw new BadCredentialsException("Invalid IP Address");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(() -> "ROLE_" + "SYS");

		// ROLE_GUEST

		// ROLE_PLATFORM

		log.info("ROLES: {}", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
		authToken = new IpWhitelistAuthenticationToken(authKey, authorities);
		authToken.setAuthenticated(true);
		return authToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {

		return (IpWhitelistAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
