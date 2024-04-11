package com.kep.platform.config.security;

import com.kep.platform.model.security.IpWhitelistAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Ip Whitelist 인증 토큰 생성 ({@link IpWhitelistAuthenticationToken})
 * {@link IpWhitelistAuthenticationProvider} 에서 인증 진행
 */
@Slf4j
public class IpWhitelistAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		// 카카오 상담톡
		if (request.getRequestURI().contains("/api/v1/kakao-counsel-talk/")) {
			// TODO: X-Origin-Host 등 헤더 파싱
			String userIp = request.getRemoteAddr();
			log.info("IP WHITELIST FILTER, KAKAO COUNSEL, IP: {}", userIp);
			IpWhitelistAuthenticationToken authToken = new IpWhitelistAuthenticationToken(userIp, AuthorityUtils.NO_AUTHORITIES);
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}

		// 솔루션
		else if (request.getRequestURI().contains("/api/v1/portal/")) {
			String userIp = request.getRemoteAddr();
			log.info("IP WHITELIST FILTER, SOLUTION, IP: {}", userIp);
			IpWhitelistAuthenticationToken authToken = new IpWhitelistAuthenticationToken(userIp, AuthorityUtils.NO_AUTHORITIES);
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}

		filterChain.doFilter(request, response);
	}
}
