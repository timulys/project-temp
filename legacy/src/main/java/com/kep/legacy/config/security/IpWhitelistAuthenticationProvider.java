package com.kep.legacy.config.security;

import com.kep.legacy.config.property.CoreProperty;
import com.kep.legacy.model.security.IpWhitelistAuthenticationToken;
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
	private CoreProperty coreProperty;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		IpWhitelistAuthenticationToken authToken = (IpWhitelistAuthenticationToken) authentication;
		String authKey = authToken.getKey();
		log.info("IP WHITELIST AUTH PROVIDER, KEY: {}", authKey);

		// ROLE by Ip
		List<String> whitelist = coreProperty.getWhitelist();

		List<GrantedAuthority> authorities = new ArrayList<>();

		// ROLE_SYS
		// TODO: subnet ip pattern
		if (!whitelist.contains(authKey)) {
			log.info("Invalid IP Address");
			throw new BadCredentialsException("Invalid IP Address");
		} else {
			authorities.add(() -> "ROLE_" + "SYS");
		}

		// ROLE_LEGACY
		// TODO: 기간계 IP 목록
		if (!whitelist.contains(authKey)) {
			log.info("Invalid IP Address");
			throw new BadCredentialsException("Invalid IP Address");
		} else {
			authorities.add(() -> "ROLE_" + "LEGACY");
		}

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
