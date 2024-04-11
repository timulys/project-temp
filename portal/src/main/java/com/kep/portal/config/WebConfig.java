package com.kep.portal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * 웹 설정
 */
@Configuration
@Profile(value = {"local"})
public class WebConfig implements WebMvcConfigurer {

	@Resource
	private QueryStringArgumentResolver queryStringArgumentResolver;

	// TODO: DELETEME, 프론트 시작시 삭제
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry
				.addResourceHandler("/webjars/**", "/static/**", "/upload/**")
				.addResourceLocations("/webjars/", "/resources/static/", "file:/opt/storage/")
				.resourceChain(false);
		registry.setOrder(1);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

		WebMvcConfigurer.super.addArgumentResolvers(resolvers);
		resolvers.add(queryStringArgumentResolver);
	}
}
