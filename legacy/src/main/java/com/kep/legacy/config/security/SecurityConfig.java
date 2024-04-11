package com.kep.legacy.config.security;

import com.kep.legacy.config.property.CoreProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * Security 설정
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

	@Value("${spring.security.debug:false}")
	boolean securityDebug;

	@Resource
	private IpWhitelistAuthenticationProvider ipWhitelistAuthenticationProvider;
	@Resource
	private CoreProperty coreProperty;
	@Resource
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	@Resource
	private CustomAccessDeniedHandler customAccessDeniedHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public IpWhitelistAuthenticationFilter ipWhitelistAuthenticationFilter() {
		return new IpWhitelistAuthenticationFilter();
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {

		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.parentAuthenticationManager(null)
				.authenticationProvider(ipWhitelistAuthenticationProvider)
				.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// @formatter:off
		http
				.httpBasic().disable()
				.formLogin().disable()
				.csrf().disable()
				.addFilterBefore(ipWhitelistAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

//				.mvcMatchers("/**")
//				.access("hasIpAddress('192.168.0/24') or hasIpAddress('127.0.0.1') or hasIpAddress('::1')")

				.and()
				.authorizeRequests()

				.mvcMatchers("/api/v1/mock/**")
				.hasAnyRole("SYS", "LEGACY")

				.mvcMatchers("/actuator/**")
				.hasAnyRole("SYS")

				.mvcMatchers("/api/v1/portal/**")
				.hasAnyRole("SYS")

				.mvcMatchers("/api/v1/legacy/**")
				.hasAnyRole("LEGACY")

				.anyRequest()
				.hasAnyRole("SYS")
		;

		http
				.exceptionHandling()
				.accessDeniedHandler(customAccessDeniedHandler)
				.authenticationEntryPoint(customAuthenticationEntryPoint)
		;
		// @formatter:on

		return http.build();
	}
}
