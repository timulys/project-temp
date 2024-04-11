package com.kep.platform;

import com.kep.platform.config.property.CoreProperty;
import com.kep.platform.config.property.KaKaoSyncProperties;
import com.kep.platform.config.property.PlatformProperty;
import com.kep.platform.config.property.PortalProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableConfigurationProperties({
	CoreProperty.class, 
	PlatformProperty.class, 
	PortalProperty.class,
	KaKaoSyncProperties.class // bnk 커스트텀 url 추가된 부분
	})
@EnableScheduling
public class PlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformApplication.class, args);
	}

}
