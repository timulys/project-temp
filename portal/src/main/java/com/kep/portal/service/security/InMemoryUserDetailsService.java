package com.kep.portal.service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * TODO: DELETEME
 */
@Deprecated
@Configuration
@Profile("auth-memory")
public class InMemoryUserDetailsService {

	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("member")
				.password(passwordEncoder.encode("memberPass"))
				.roles("MEMBER")
				.build());
		manager.createUser(User.withUsername("admin")
				.password(passwordEncoder.encode("adminPass"))
				.roles("MEMBER", "ADMIN")
				.build());
		return manager;
	}
}
