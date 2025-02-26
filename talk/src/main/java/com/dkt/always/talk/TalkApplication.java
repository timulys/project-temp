package com.dkt.always.talk;

import com.dkt.always.talk.config.property.KakaoBizTalkProperty;
import com.dkt.always.talk.config.property.PlatformProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties({
		KakaoBizTalkProperty.class,
		PlatformProperty.class
})
@EnableFeignClients
public class TalkApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(TalkApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(TalkApplication.class, args);
	}

}
