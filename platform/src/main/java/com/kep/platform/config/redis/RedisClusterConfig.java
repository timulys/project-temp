package com.kep.platform.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.core.convert.MappingConfiguration;
import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis (Cluster) 서버 설정
 */
//@Configuration
//@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
//@Profile({"live"})
public class RedisClusterConfig {

	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private int port;
	@Value("${spring.redis.password}")
	private String password;

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {

		RedisClusterConfiguration redisConfiguration = new RedisClusterConfiguration();
		RedisNode redisNode = new RedisNode(host, port);
		redisConfiguration.addClusterNode(redisNode);
		redisConfiguration.setPassword(password);

		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration);
		lettuceConnectionFactory.setDatabase(0);
		return lettuceConnectionFactory;
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
}
