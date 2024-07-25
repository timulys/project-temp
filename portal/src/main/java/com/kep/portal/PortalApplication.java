package com.kep.portal;

import com.kep.portal.config.property.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({CoreProperty.class, SocketProperty.class, PortalProperty.class,
		PlatformProperty.class, CodeProperty.class, SystemMessageProperty.class, ModeProperty.class , WatcherProperty.class})
@EnableScheduling
public class PortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalApplication.class, args);
	}

}
