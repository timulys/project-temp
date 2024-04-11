package com.kep.legacy;

import com.kep.legacy.config.property.CoreProperty;
import com.kep.legacy.config.property.LegacyProperty;
import com.kep.legacy.config.property.PortalProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({CoreProperty.class, PortalProperty.class, LegacyProperty.class})
@EnableScheduling
public class LegacyApplication {

	public static void main(String[] args) {
		SpringApplication.run(LegacyApplication.class, args);
	}

}
