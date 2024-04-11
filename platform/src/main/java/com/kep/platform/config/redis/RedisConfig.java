package com.kep.platform.config.redis;

import com.kep.platform.config.property.PlatformProperty;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.core.convert.MappingConfiguration;
import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.RedisSessionRepository;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;

/**
 * Redis 서버 설정
 */
@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
//@Profile({"local", "cc-dev", "dev", "stg"})
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private int port;
	@Value("${spring.redis.password}")
	private String password;

	@Value("${spring.redis.cluster.nodes}")
	private List<String> nodes;


	@Resource
	private PlatformProperty platformProperty;
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		if("cluster".equals(platformProperty.getRedisMode())){
			return connectionClusterFactory();
		}

		return connectionFactory();
	}


	@Bean
	public RedisKeyValueAdapter redisKeyValueAdapter() {

		RedisMappingContext mappingContext = new RedisMappingContext(new MappingConfiguration(
				new IndexConfiguration(), new KeyspaceConfiguration()));
		return new RedisKeyValueAdapter(redisTemplate(), mappingContext);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new StringRedisSerializer());
		return redisTemplate;
	}
	/**
	 *
	 * @return
	 */
	protected LettuceConnectionFactory connectionFactory(){
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);
		redisStandaloneConfiguration.setPassword(password);
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	/**
	 * mode : cluster
	 * @return
	 */
	protected LettuceConnectionFactory connectionClusterFactory(){
		RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(nodes);
		redisClusterConfiguration.setPassword(password);

		// topology 자동 업데이트 옵션 추가
		// enablePeriodicRefresh(tolpology 정보 감시 텀) default vaule : 60s
		ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
				.refreshPeriod(Duration.ofMinutes(1))
				.enableAllAdaptiveRefreshTriggers()
				.build();

		ClientOptions clientOptions = ClusterClientOptions.builder()
				.topologyRefreshOptions(clusterTopologyRefreshOptions)
				.build();

		// topology 옵션 및 timeout 세팅
		LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
				.clientOptions(clientOptions)
				.readFrom(ReadFrom.REPLICA_PREFERRED)
				.build();

		return new LettuceConnectionFactory(redisClusterConfiguration , clientConfiguration);
	}
}
