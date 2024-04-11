package com.kep.platform.service.kakao.sync;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.platform.AuthorizeType;
import com.kep.platform.client.PortalClient;
import com.kep.platform.config.property.CoreProperty;
import com.kep.platform.service.SendToPortalProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class KakaoSyncService {

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private RestTemplate externalRestTemplate;
	@Resource
	private OAuth2ClientProperties oAuth2ClientProperties;
	@Resource
	private CoreProperty coreProperty;
	@Resource
	private PortalClient portalClient;
	@Resource
	private SendToPortalProducer producer;

	@Value("${serviceIdKey.bnk}")
	private String serviceIdKey;

	public String authorized(@NotEmpty String code, @NotNull Map<String, String> params) throws Exception {
		// 주어진 params의 'state'값 처리
		processParamsState(params);
		String accessToken = getToken(code);
		if (!ObjectUtils.isEmpty(accessToken)) {
			// 액세스 토큰을 사용해 사용자 정보를 얻어옵니다.
			Map<String, Object> userInfo = getUserInfo(accessToken);
			// 추가 파라미터 처리
			Map<String, Object> extra = processExtraParams(params, userInfo);
			// 사용자 정보 인증
			portalClient.authorized(AuthorizeType.kakao_sync, userInfo, System.currentTimeMillis());
			// 추가 파라미터를 기반으로 반환 URL 생성
			return generateReturnUrl(extra);
		}
		return "";
	}

	private void processParamsState(Map<String, String> params) throws Exception {
		// params의 'state' 부분이 비어 있지 않으면
		if(!ObjectUtils.isEmpty(params.get("state"))){
			log.info("KAKAO SYNC, AUTHORIZED, STATE: {}", params.get("state"));
			Map<String, String> extraParams = objectMapper.readValue(params.get("state"), new TypeReference<Map<String, String>>() {});
			// API 요청을 위해 추출한 파라미터를 준비
			prepareExtraParams(extraParams);
			// 추출된 파라미터를 원래 파라미터 map에 추가하고 'state' 제거
			params.putAll(extraParams);
			params.remove("state");
		}
	}

	private void prepareExtraParams(Map<String, String> extraParams) {
		// 'mgt_ymd' 키 제거.
		extraParams.remove("mgt_ym");
		// 'seqno' 키의 값을 'o'로 변경
		if(extraParams.containsKey("seqno")) {
			extraParams.put("o", extraParams.get("seqno"));
			extraParams.remove("seqno");
		}
	}

	private Map<String, Object> processExtraParams(Map<String, String> params,  Map<String, Object> userInfo) {
		// 추가 파라미터를 위한 map을 userInfo에 추가
		Map<String, Object> extra = new HashMap<>(params);
		extra.remove("code");
		userInfo.put("extra", extra);
		return extra;
	}

	private String generateReturnUrl(Map<String, Object> extra) {
		// 'extra'가 비어 있지 않으면 반환 URL 생성
		if(!ObjectUtils.isEmpty(extra)) {
			// 추가된 데이터로 URL 파라미터를 준비
			String urlParam = prepareUrlParam(extra);
			return coreProperty.kakaoCounselTalkBaseUrl + serviceIdKey + urlParam;
		}
		return "";
	}

	private String prepareUrlParam(Map<String, Object> extra) {
		// 추가 값을 URL 파라미터 문자열로 연결
		String urlParam = "";
		for (String extraKey : extra.keySet()) {
			if(!ObjectUtils.isEmpty(urlParam)) {
				urlParam += "__";
			}
			String extraValue = (String) extra.get(extraKey);
			Matcher matcher = Pattern.compile("([a-z])_([a-z])").matcher(extraKey);
			StringBuffer sb = new StringBuffer();
			// camel 표기로 변환
			while (matcher.find()) {
				String format = String.format("%s%s", matcher.group(1).toLowerCase(), matcher.group(2).toUpperCase());
				matcher.appendReplacement(sb,format);
			}
			StringBuffer stringBuffer = matcher.appendTail(sb);
			extraKey = stringBuffer.toString();
			urlParam += extraKey + "_" + extraValue;
		}
		// URL 파라미터 문자열 반환
		return urlParam;
	}


	@Nullable
	public String getToken(@NotEmpty String code) {

		Map<String, OAuth2ClientProperties.Registration> registrations = oAuth2ClientProperties.getRegistration();
		Map<String, OAuth2ClientProperties.Provider> providers = oAuth2ClientProperties.getProvider();
		OAuth2ClientProperties.Registration registration = registrations.get("kakao-sync");
		OAuth2ClientProperties.Provider provider = providers.get("kakao-sync");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("grant_type", registration.getAuthorizationGrantType());
		map.add("client_id", registration.getClientId());
		map.add("redirect_uri", registration.getRedirectUri());
		map.add("code", code);
		map.add("client_secret", registration.getClientSecret());
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		ResponseEntity<Map<String, Object>> response = externalRestTemplate.exchange(provider.getTokenUri(),
				HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Object>>() {});
		log.info("{}", response.getBody());
		Map<String, Object> responseBody = response.getBody();
		if (responseBody != null) {
			if (!ObjectUtils.isEmpty(responseBody.get("access_token"))) {
				String accessToken = (String) responseBody.get("access_token");
				log.info("accessToken: {}", accessToken);
				return accessToken;
			}
		}

		return null;
	}

	public Map<String, Object> getUserInfo(@NotEmpty String accessToken) {

		Map<String, OAuth2ClientProperties.Provider> providers = oAuth2ClientProperties.getProvider();
		OAuth2ClientProperties.Provider provider = providers.get("kakao-sync");

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<String> request = new HttpEntity<>(headers);

		ResponseEntity<Map<String, Object>> response = externalRestTemplate.exchange(provider.getUserInfoUri(),
				HttpMethod.GET, request, new ParameterizedTypeReference<Map<String, Object>>() {});

		log.info("{}", response.getBody());
		Map<String, Object> responseBody = response.getBody();
		if (responseBody != null) {
			return responseBody;
		} else {
			return Collections.emptyMap();
		}
	}
}