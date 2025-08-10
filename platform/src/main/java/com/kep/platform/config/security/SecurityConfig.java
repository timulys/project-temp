package com.kep.platform.config.security;

import com.kep.platform.config.property.PlatformProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

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
	private ApiKeyAuthenticationProvider apiKeyAuthenticationProvider;
	@Resource
	private PlatformProperty platformProperty;
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
	public ApiKeyAuthenticationFilter apiKeyAuthenticationFilter() {
		return new ApiKeyAuthenticationFilter(platformProperty);
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {

		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.parentAuthenticationManager(null)
				.authenticationProvider(ipWhitelistAuthenticationProvider)
				.authenticationProvider(apiKeyAuthenticationProvider)
				.build();
	}

	@Bean
	@Order(1)
	public SecurityFilterChain probeChain(HttpSecurity http) throws Exception {
		// Only match health/readiness/liveness and simple root healthz
		http
				.requestMatcher(new OrRequestMatcher(
						new AntPathRequestMatcher("/"),
						new AntPathRequestMatcher("/healthz"),
						new AntPathRequestMatcher("/platform/healthz"),
						new AntPathRequestMatcher("/actuator/health"),
						new AntPathRequestMatcher("/actuator/health/**")
				))
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.anyRequest().permitAll();

		// IMPORTANT: do NOT register custom filters here (ip whitelist, api key)
		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// @formatter:off
		http
				.httpBasic().disable()
				.formLogin().disable()
				.csrf().disable()
				.addFilterBefore(ipWhitelistAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(apiKeyAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)

//				.mvcMatchers("/**")
//				.access("hasIpAddress('192.168.0/24') or hasIpAddress('127.0.0.1') or hasIpAddress('::1')")

				.and()
				.authorizeRequests()

				.mvcMatchers("/api/v1/kakao-counsel-talk/**")
				.permitAll()

				.mvcMatchers("/auth/**")
				.permitAll()

				.mvcMatchers("/api/v1/mock/**")
				.permitAll()

				.mvcMatchers("/actuator/**")
				.hasAnyRole("SYS")

//				.mvcMatchers("/api/v1/portal/**")
				.mvcMatchers("/api/v1/counsel-portal/**")
				.hasAnyRole("SYS")

				.mvcMatchers("/api-docs/**" , "/swagger-ui/**").permitAll()
				.mvcMatchers("/actuator/health", "/actuator/health/**", "/healthz", "/platform/healthz", "/").permitAll()
				.mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.anyRequest()
				.authenticated()
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
