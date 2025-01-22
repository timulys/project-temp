package com.dkt.always.talk.service.template.impl;

import com.dkt.always.talk.config.property.KakaoBizTalkProperty;
import com.dkt.always.talk.config.property.PlatformProperty;
import com.dkt.always.talk.service.BizTalkCommonService;
import com.dkt.always.talk.service.template.TemplateExternalService;
import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.BizTalkResponseDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TemplateCategoryResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Biz Message Center Template API Call External Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateExternalServiceImpl extends BizTalkCommonService implements TemplateExternalService {
    /** Template API URL */
    // 템플릿 등록
    public static final String TEMPLATE_CREATE_PATH = "/resell/template/create";
    // 템플릿 수정
    public static final String TEMPLATE_MODIFY_PATH = "/resell/template/modify";
    // 템플릿 검수요청 취소
    public static final String TEMPLATE_CANCEL_PATH = "/resell/template/cancel/request/";
    // 템플릿 카테고리 전체 조회
    public static final String TEMPLATE_CATEGORY_ALL_PATH = "/resell/template/category/all";

    /** Autowired Components */
    private final WebClient kakaoBizTalkWebClient;
    private final PlatformProperty platformProperty;
    private final KakaoBizTalkProperty kakaoBizTalkProperty;

    @Override
    @CircuitBreaker(name = "default", fallbackMethod = "templateExternalCallFailedMethod")
    public ResponseEntity<? super BizTalkResponseDto> createTemplate(@Valid KakaoBizMessageTemplatePayload requestDto) {
        BizTalkResponseDto<KakaoBizMessageTemplatePayload> response = kakaoBizTalkWebClient.post()
                .uri(String.format("%s/%s", getTemplateRequestUrl(platformProperty, TEMPLATE_CREATE_PATH, "v1"), requestDto.getSenderKey()))
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BizTalkResponseDto<KakaoBizMessageTemplatePayload>>() {})
                .block();
        log.info("create template response = {}", response);

        if (!response.getCode().contains("200"))
            return ResponseDto.customFailedMessage(response.getCode(), response.getMessage());

        return ResponseEntity.ok(response);
    }

    @Override
    @CircuitBreaker(name = "default", fallbackMethod = "templateExternalCallFailedMethod")
    public ResponseEntity<? super BizTalkResponseDto> getCategoryList() {
        BizTalkResponseDto<List<TemplateCategoryResponseDto>> response = kakaoBizTalkWebClient.get()
                .uri(getTemplateRequestUrl(platformProperty, TEMPLATE_CATEGORY_ALL_PATH, "v1"))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BizTalkResponseDto<List<TemplateCategoryResponseDto>>>() {})
                .block();
        log.info("template response = {}", response);

        if (!response.getCode().contains("200"))
            return ResponseDto.customFailedMessage(response.getCode(), response.getMessage());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> getClientId() {
        return ResponseEntity.ok(kakaoBizTalkProperty.getClientId());
    }

    @Override
    @CircuitBreaker(name = "default", fallbackMethod = "templateExternalCallFailedMethod")
    public ResponseEntity<? super BizTalkResponseDto> updateTemplate(KakaoBizMessageTemplatePayload requestDto) {
        BizTalkResponseDto<KakaoBizMessageTemplatePayload> response = kakaoBizTalkWebClient.put()
                .uri(getTemplateRequestUrl(platformProperty, TEMPLATE_MODIFY_PATH, "v1"))
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BizTalkResponseDto<KakaoBizMessageTemplatePayload>>() {})
                .block();
        log.info("modify template response = {}", response);

        if (!response.getCode().contains("200"))
            return ResponseDto.customFailedMessage(response.getCode(), response.getMessage());

        return ResponseEntity.ok(response);
    }

    @Override
    @CircuitBreaker(name = "default", fallbackMethod = "templateExternalCallFailedMethod")
    public ResponseEntity<? super BizTalkResponseDto> cancelTemplate(String profileKey, String templateCode) {
        BizTalkResponseDto<KakaoBizMessageTemplatePayload> response = kakaoBizTalkWebClient.put()
                .uri(
                        String.format("%s/%s/%s",
                                getTemplateRequestUrl(platformProperty, TEMPLATE_CANCEL_PATH, "v1"),
                                profileKey,
                                templateCode
                        )
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BizTalkResponseDto<KakaoBizMessageTemplatePayload>>() {})
                .block();
        log.info("cancel template request response = {}", response);

        if (!response.getCode().contains("200"))
            return ResponseDto.customFailedMessage(response.getCode(), response.getMessage());

        return ResponseEntity.ok(response);
    }

    /////////////////////////// private methods ///////////////////////////
    private ResponseEntity<ResponseDto> templateExternalCallFailedMethod(Throwable throwable) {
        log.error("BizMessageCenter API Call Failed Fallback: {}", throwable.getMessage());
        return ResponseDto.bizCenterCallFailedMessage();
    }
}
