package com.kep.portal.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

/**
 * QueryDSL 설정
 */
@Configuration
public class QueryDslConfig {

	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {

		return new JPAQueryFactory(entityManager);
	}
}
