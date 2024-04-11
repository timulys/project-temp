package com.kep.portal.config.session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.time.Duration;

/**
 * 세션 설정 (Redis), 프론트 로컬 개발 대응 (로컬서버, 원격서버 모두 https 필요)
 */
//@EnableRedisHttpSession(redisNamespace = "${spring.session.redis.namespace}")
@Configuration
@EnableSpringHttpSession
@Profile({"cc-dev"})
public class RedisSessionSameSiteConfig {

	@Value("${spring.redis.host}")
	private String redisHost;
	@Value("${spring.redis.port}")
	private int redisPort;
	@Value("${spring.session.redis.namespace}")
	private String redisKeyNamespace;
	@Value("${spring.session.timeout}")
	private Integer maxInactiveInterval;

	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory() {

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(redisHost);
		redisStandaloneConfiguration.setPort(redisPort);
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean
	public RedisOperations<String, Object> sessionRedisOperations() {

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(lettuceConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		return template;
	}

	@Bean
	public RedisSessionRepository sessionRepository(RedisOperations<String, Object> sessionRedisOperations) {

		RedisSessionRepository redisSessionRepository = new RedisSessionRepository(sessionRedisOperations);
		redisSessionRepository.setRedisKeyNamespace(redisKeyNamespace);
		redisSessionRepository.setDefaultMaxInactiveInterval(Duration.ofSeconds(maxInactiveInterval));
		return redisSessionRepository;
	}

	@Bean
	public static ConfigureRedisAction configureRedisAction() {

		return ConfigureRedisAction.NO_OP;
	}

	@Bean
	public CookieSerializer cookieSerializer() {

		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setCookieName("JSESSIONID");
		serializer.setCookiePath("/");
//		serializer.setDomainName(portalProperty.getCookieDomain());
		serializer.setSameSite("None");
		serializer.setUseSecureCookie(true);
		serializer.setUseHttpOnlyCookie(false);
		return serializer;
	}
}
