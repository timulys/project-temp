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
 * 웹 설정 (CORS 허용)
 */
@Configuration
@Profile(value = {"!local"})
public class WebCrossOriginConfig implements WebMvcConfigurer {

	@Resource
	private QueryStringArgumentResolver queryStringArgumentResolver;

	// TODO: DELETEME, 프론트 시작시 삭제
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("/webjars/")
				.resourceChain(false);
		registry.setOrder(1);
	}

	// TODO: DELETEME, 프론트 테스트시 필요할 듯
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowCredentials(true)
				.allowedOriginPatterns("*")
//				.allowedOriginPatterns("https://front.codeclick.co.kr", "http://connect-branch-front.kep.k9d.in")
//				.allowedOrigins("https://front.codeclick.co.kr", "http://connect-branch-front.kep.k9d.in")
				.allowedHeaders("*")
				.allowedMethods("*")
		;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

		WebMvcConfigurer.super.addArgumentResolvers(resolvers);
		resolvers.add(queryStringArgumentResolver);
	}
}
