package com.kep.platform.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 암호화
 */
@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

	/**
	 * 설정 파일 암호화
	 */
	@Bean(name = "propertyEncryptor") // jasypt.encryptor.bean in application.yml 에서 등록
	public StringEncryptor propertyEncryptor() {

		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword("password"); // 암호화 키
		config.setAlgorithm("PBEWithMD5AndDES"); // 알고리즘
		config.setKeyObtentionIterations(1000);
		config.setPoolSize(2);
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);

		return encryptor;
	}
}
