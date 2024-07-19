package com.kep.platform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 외부망이 차단된 환경일 경우, 프록시로 사용 가능
 */
@Tag(name = "프록시 API", description = "외부망 차단된 환경에서 사용")
@Slf4j
public class ProxyController {

	@Resource
	private RestTemplate restTemplate;

	/**
	 * 카카오 상담톡, 파일 프록시
	 */
	@Tag(name = "프록시 API")
	@Operation(summary = "카카오 상담톡, 파일 프록시")
	@GetMapping("/proxy/kakao")
	@ResponseBody
	public StreamingResponseBody getKakaoFile(@RequestParam("url") String url) {

		log.info("REQUEST KAKAO COUNSEL FILE: URL: {}", url);

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		ResponseEntity<org.springframework.core.io.Resource> responseEntity = restTemplate.exchange(
				url, HttpMethod.GET, request, org.springframework.core.io.Resource.class);

		try {
			if (responseEntity != null && responseEntity.getBody() != null) {
				InputStream inputStream = responseEntity.getBody().getInputStream();
				return (outputStream) -> {
					redirectStream(inputStream, outputStream);
				};
			}
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}

		return null;
	}

	/**
	 * InputStream to OutputStream
	 *
	 * @param inputStream InputStream
	 * @param outputStream OutputStream
	 * @throws IOException
	 */
	private void redirectStream(InputStream inputStream, OutputStream outputStream) throws IOException {

		byte[] buffer = new byte[8192];
		int readBytes;

		while ((readBytes = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, readBytes);
		}

		outputStream.flush();
	}
}
