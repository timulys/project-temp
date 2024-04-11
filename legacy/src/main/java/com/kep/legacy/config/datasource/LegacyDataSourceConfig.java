package com.kep.legacy.config.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 기간계 Datasource
 */
@Configuration
@Profile({"!jndi"})
@MapperScan(value="com.kep.legacy.repository", annotationClass= LegacyMapper.class, sqlSessionFactoryRef="legacySqlSessionFactory")
@EnableTransactionManagement
public class LegacyDataSourceConfig {

	@Bean(name="legacyDataSource")
	@ConfigurationProperties(prefix="spring.datasource.legacy")
	public DataSource legacyDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name="legacySqlSessionFactory")
	public SqlSessionFactory legacySqlSessionFactoryBean(@Autowired @Qualifier("legacyDataSource") DataSource legacyDataSource, ApplicationContext applicationContext)
			throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(legacyDataSource);
		factoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-legacy.xml"));
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/legacy/**/*.xml"));
		return factoryBean.getObject();
	}

	@Bean(name="legacySqlSession")
	public SqlSessionTemplate legacySqlSession(@Autowired @Qualifier("legacySqlSessionFactory") SqlSessionFactory legacySqlSessionFactory) {
		return new SqlSessionTemplate(legacySqlSessionFactory);
	}

	@Bean(name="legacyTransactionManager")
	public DataSourceTransactionManager legacyTransactionManager(@Autowired @Qualifier("legacyDataSource") DataSource legacyDataSource) {
		return new DataSourceTransactionManager(legacyDataSource);
	}
}
