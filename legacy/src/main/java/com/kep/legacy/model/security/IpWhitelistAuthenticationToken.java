package com.kep.legacy.model.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;

import java.util.Collection;

/**
 * IP Whitelist 인증 토큰
 */
@Transient
@Getter
public class IpWhitelistAuthenticationToken extends AbstractAuthenticationToken {

	private String key;

	public IpWhitelistAuthenticationToken(String key, Collection<GrantedAuthority> authorities) {

		super(authorities);
		this.key = key;
	}

	@Override
	public Object getCredentials() {
		return key;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}
}
