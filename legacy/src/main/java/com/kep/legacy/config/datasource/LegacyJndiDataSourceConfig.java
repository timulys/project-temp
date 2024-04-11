package com.kep.legacy.config.datasource;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 기간계 Datasource (JNDI)
 */
@Configuration
@Profile({"jndi"})
@MapperScan(value="com.kep.legacy.repository", annotationClass=LegacyMapper.class, sqlSessionFactoryRef="legacySqlSessionFactory")
@EnableTransactionManagement
public class LegacyJndiDataSourceConfig {

	@Bean
	public ServletWebServerFactory tomcatFactory() {

		return new TomcatServletWebServerFactory() {
			@Override
			protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
				tomcat.enableNaming();
				return super.getTomcatWebServer(tomcat);
			}

			@Override
			protected void postProcessContext(Context context) {
				ContextResource resource = new ContextResource();
				resource.setName("jdbc/legacy");
				resource.setType(DataSource.class.getName());
				resource.setProperty("driverClassName", "oracle.jdbc.OracleDriver");
				resource.setProperty("url", "jdbc:oracle:thin:@211.43.15.158:7521:orcl");
				resource.setProperty("username", "legacy");
				resource.setProperty("password", "legacy!#%");
//				resource.setProperty("factory", "com.zaxxer.hikari.HikariDataSource");
//				resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
//				resource.setProperty("type", "javax.sql.DataSource");

				context.getNamingResources().addResource(resource);
			}
		};
	}

	@Bean("legacyDataSource")
	public DataSource legacyDataSource() throws IllegalArgumentException, NamingException {
		JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
		bean.setJndiName("java:comp/env/jdbc/legacy");
		//bean.setResourceRef(true); // this was previously uncommented
		bean.setProxyInterface(DataSource.class);
		//bean.setLookupOnStartup(false); // this was previously uncommented
		bean.afterPropertiesSet();
		return (DataSource)bean.getObject();
//		return (DataSource) new JndiDataSourceLookup().getDataSource("java:comp/env/jdbc/legacy");
	}

//	@Bean(name="legacyDataSource")
//	@ConfigurationProperties(prefix="legacy.datasource.legacy")
//	public DataSource legacyDataSource() {
//		return DataSourceBuilder.create().build();
//	}

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
