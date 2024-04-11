package com.kep.portal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class CsrfTokenController {

	@GetMapping("/csrf")
	public @ResponseBody String getCsrfToken(HttpServletRequest request, HttpSession httpSession) {

		log.info("httpSession: {}", httpSession.getId());
		CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		return csrf.getToken();
	}
}
