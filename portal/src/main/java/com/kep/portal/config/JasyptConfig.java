package com.kep.portal.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 암호화
 */
@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

	/**
	 * TODO: 고객사 모듈 암호화
	 */
	public StringEncryptor legacyEncryptor() {

		return null;
	}

	/**
	 * 솔루션 기본 암호화
	 */
	@Bean(name = "defaultEncryptor")
	@Primary
	public StringEncryptor defaultEncryptor() {

		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setProvider(new BouncyCastleProvider());
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword("password"); // 암호화 키
		config.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC"); // 알고리즘
		config.setKeyObtentionIterations(1000);
		config.setPoolSize(2);
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);

		return encryptor;
	}

	/**
	 * 솔루션 기본 암호화 (fixed result)
	 */
	@Bean(name = "fixedEncryptor")
	public StringEncryptor fixedEncryptor() {

		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setProvider(new BouncyCastleProvider());
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword("password"); // 암호화 키
		config.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC"); // 알고리즘
		config.setKeyObtentionIterations(1000);
		config.setPoolSize(2);
		config.setSaltGeneratorClassName("org.jasypt.salt.ZeroSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);

		return encryptor;
	}

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
