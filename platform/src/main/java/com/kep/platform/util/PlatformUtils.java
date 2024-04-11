package com.kep.platform.util;

import javax.validation.constraints.NotEmpty;

/**
 * 플랫폼 유틸리티
 */
public class PlatformUtils {

	/**
	 * HTML 태그 이스케이프
	 */
	public static String escapeHtml(@NotEmpty String html) {

		if(html == null)
			return html;

		return html.replaceAll("&lt;", "<")
				.replaceAll("&gt;", ">");
	}
}
