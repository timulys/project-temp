package com.kep.portal.config.session;

import com.kep.portal.config.property.PortalProperty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.core.convert.MappingConfiguration;
import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * 세션 설정 (Redis)
 */
//@EnableRedisHttpSession(redisNamespace = "${spring.session.redis.namespace}")
@Configuration
@EnableSpringHttpSession
@Profile({"!cc-dev"})
public class RedisSessionConfig extends RedisConfig{

	@Value("${spring.session.redis.namespace}")
	private String redisKeyNamespace;
	@Value("${spring.session.timeout}")
	private Integer maxInactiveInterval;

	@Resource
	private PortalProperty portalProperty;

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		if("cluster".equals(portalProperty.getRedisMode())){
			return connectionClusterFactory();
		}
		return connectionFactory();
	}


	@Bean
	public RedisTemplate<String, Object> redisTemplate() {

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}

	@Bean
	public RedisKeyValueAdapter redisKeyValueAdapter() {

		RedisMappingContext mappingContext = new RedisMappingContext(new MappingConfiguration(
				new IndexConfiguration(), new KeyspaceConfiguration()));
		return new RedisKeyValueAdapter(redisTemplate(), mappingContext);
	}
//
//	@Bean
//	public RedisOperations<String, Object> sessionRedisOperations() {
//
//		RedisTemplate<String, Object> template = new RedisTemplate<>();
//		template.setConnectionFactory(redisConnectionFactory());
//		template.setKeySerializer(new StringRedisSerializer());
//		template.setHashKeySerializer(new StringRedisSerializer());
//		return template;
//	}

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
		return serializer;
	}
}
