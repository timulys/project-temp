package com.kep.portal.config.security;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

@ExtendWith(SpringExtension.class)
@Slf4j
class PasswordEncoderTest {

	@TestConfiguration
	static class ContextConfig {
		@Bean
		public PasswordEncoder passwordEncoder() {
			return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		}
	}

	@Resource
	private PasswordEncoder passwordEncoder;

	@Test
	void passwordEncoder() {

		String encoded = passwordEncoder.encode("userPass");
		log.info("userPass encoded: {}", encoded);

		encoded = passwordEncoder.encode("adminPass");
		log.info("adminPass encoded: {}", encoded);
	}
}
