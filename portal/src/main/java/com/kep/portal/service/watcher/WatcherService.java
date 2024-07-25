package com.kep.portal.service.watcher;

import com.kep.portal.config.property.WatcherProperty;
import com.kep.portal.model.dto.watcher.SendMsgGroupKakaoworkDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
@AllArgsConstructor
public class WatcherService {


	// API 관련 URL : https://noti.daumkakao.io/document/api
	private final WatcherProperty watcherProperty;

	@Resource
	private RestTemplate externalRestTemplate;


	// eddie.j 유저 권한 , 호출 된 URL 오류 메세지만 일단 전송

	/**
	 * @수정일자		/ 수정자		 	/ 수정내용
	 * 2024.07.25	/ eddie.j	    / 초기작성 : watch_center 사용 프로토타입 개발
	 * @param ex
	 * @param webRequest
	 */
	public void  exceptionWatcherSendMsgGroupKakaowork (Exception ex, WebRequest webRequest) {
		String sendUrl = watcherProperty.getApiBaseUrl() + watcherProperty.getGroupKakaowork();

		String msg = this.setMessage(ex,webRequest);

		SendMsgGroupKakaoworkDto sendMsgGroupKakaoworkDto = SendMsgGroupKakaoworkDto.builder()
															.msg(msg)
															.to(watcherProperty.getGroupId())
															.build();

		externalRestTemplate.postForObject(sendUrl, sendMsgGroupKakaoworkDto, String.class);
	}


	private String setMessage(Exception ex, WebRequest webRequest) {
		String msg = null;

		try {
			msg = "🔔 [올웨이즈 장애 알림]\n" +
				  "[유저 권한] = " + webRequest.getUserPrincipal().getName() + "\n" +
				  "[호출 URL] = " + this.getUrlFromWebRequest(webRequest) + "\n" +
				  "[오류 메세지] = " + ex.getMessage();
		}
		catch (Exception setMsgEx){
			msg = "🔔 [올웨이즈 장애 알림]\n" +
				  "[시스템 오류 메세지] = " + ex.getMessage() + "\n" +
			      "[setMessage 오류 메세지] = " + setMsgEx.getMessage();
		}
		return msg;
	}

	private String getUrlFromWebRequest(WebRequest webRequest) {
		if (webRequest instanceof ServletWebRequest) {
			ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
			HttpServletRequest request = servletWebRequest.getRequest();
			return request.getRequestURL().toString();
		}
		return null;
	}

}
