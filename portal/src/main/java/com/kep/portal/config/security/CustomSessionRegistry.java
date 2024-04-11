package com.kep.portal.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

@Configuration
public class CustomSessionRegistry {
	@Bean
	public SessionRegistry sessionRegistry() {
	        return new SessionRegistryImpl();
	    }
}
