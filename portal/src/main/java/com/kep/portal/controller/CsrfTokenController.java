package com.kep.portal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Tag(name = "csrf token API")
@Controller
@Slf4j
public class CsrfTokenController {

	@Tag(name = "csrf token API")
	@Operation(summary = "토큰 조회")
	@GetMapping("/csrf")
	public @ResponseBody String getCsrfToken(HttpServletRequest request, HttpSession httpSession) {

		log.info("httpSession: {}", httpSession.getId());
		CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		return csrf.getToken();
	}
}
