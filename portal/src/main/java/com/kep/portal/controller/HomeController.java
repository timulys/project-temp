package com.kep.portal.controller;

import com.kep.portal.config.property.PortalProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Random;

/**
 * TODO: DELETEME, 프론트 시작시 삭제
 */
@Controller
@Slf4j
public class HomeController {

	@Resource
	private PortalProperty portalProperty;

	@RequestMapping(value = {"/", "/home"})
	public String home(HttpSession httpSession, Model model) {

		log.info("httpSession: {}", httpSession.getId());

		Integer hits = (Integer) httpSession.getAttribute("hits");
		if (hits == null) {
			hits = 0;
		}
		httpSession.setAttribute("hits", ++hits);

		model.addAttribute("issueId", new Random().nextLong());
		model.addAttribute("serviceUrl", portalProperty.getServiceUrl());

		return "home";
	}
}
