package com.kep.portal.config;

import com.kep.portal.config.property.SocketProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class SpringProfileTest {

	@Resource
	private Environment environment;
	@Resource
	private SocketProperty socketProperty;

	@Test
	void testProfile() {

		log.info("{}", environment.getActiveProfiles());
		log.info("{}", environment.getDefaultProfiles());

		log.info("{}", socketProperty.getEndpoint());
	}
}
