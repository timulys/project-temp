package com.kep.legacy.model.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;

import java.util.Collection;

/**
 * API Key 인증 토큰
 */
@Transient
@Getter
@Setter
public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

	private String key;

	public ApiKeyAuthenticationToken(String key, Collection<GrantedAuthority> authorities) {

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
