package com.kep.portal.config.debug;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Http 요청 디버그 용도
 */
@Component
@Order(-1)
@Slf4j
public class DebugFilter extends OncePerRequestFilter {

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String url = request.getRequestURI();
		log.info("{}: {}", request.getMethod(), url);

		if (log.isDebugEnabled()) {
			if (!ObjectUtils.isEmpty(request.getQueryString())) {
				url += "?" + request.getQueryString();
			}
			log.debug("{}: {}", request.getMethod(), url);

			Enumeration<String> headerNames = request.getHeaderNames();
			if (headerNames != null) {
				while (headerNames.hasMoreElements()) {
					log.debug("Header: {}", request.getHeader(headerNames.nextElement()));
				}
			}
		}

		filterChain.doFilter(request, response);
	}
}
