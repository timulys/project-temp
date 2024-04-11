package com.kep.portal.config.security;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Security 설정
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

	@Value("${spring.security.debug:false}")
	boolean securityDebug;

	@Resource
	public AuthenticationSuccessHandler restAuthenticationSuccessHandler;
	@Resource
	public AuthenticationFailureHandler restAuthenticationFailureHandler;
	@Resource
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	@Resource
	private CustomAccessDeniedHandler customAccessDeniedHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {

		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// @formatter:off
		http
				.httpBasic().disable()
//				.formLogin().disable()
				.cors().disable()
				.csrf().disable()

				.sessionManagement()
				.sessionFixation().changeSessionId()
				.maximumSessions(1)
				.maxSessionsPreventsLogin(false)
		;

		http
				.authorizeRequests()

				.mvcMatchers("/api/v1/mock/**", "/webjars/**", "/upload/**", "/static/**", "/home/**")
				.permitAll()

				.mvcMatchers("/api/v1/member/by-role/**",
						"/api/v1/event-by-platform/**")
//				.hasAnyRole("SYS")
//				.hasAnyAuthority("READ_ISSUE", "WRITE_ISSUE")
				.permitAll()

				.mvcMatchers("/proxy/**")
				.permitAll()

				.anyRequest()
				.permitAll() // dev 용
				//.authenticated() //stg용
				.and()
				.formLogin()
				.loginPage("/login")
				.successHandler(restAuthenticationSuccessHandler)
				.failureHandler(restAuthenticationFailureHandler)
//				.and()
//				.logout()
//				.logoutSuccessHandler(logoutSuccessHandler)
		;

		http
				.exceptionHandling()
				.accessDeniedHandler(customAccessDeniedHandler)
				.authenticationEntryPoint(customAuthenticationEntryPoint)
		;
		// @formatter:on

		return http.build();
	}
	@Bean
	public AuthenticationEventPublisher authenticationEventPublisher
			(ApplicationEventPublisher applicationEventPublisher) {
		return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
	}
}