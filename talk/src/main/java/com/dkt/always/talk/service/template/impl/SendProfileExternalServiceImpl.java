package com.dkt.always.talk.service.template.impl;

import com.dkt.always.talk.config.property.PlatformProperty;
import com.dkt.always.talk.service.BizTalkCommonService;
import com.dkt.always.talk.service.template.SendProfileExternalService;
import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.BizTalkResponseDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.SendProfileResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Biz Message Center Send Profile Check API Call External Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SendProfileExternalServiceImpl extends BizTalkCommonService implements SendProfileExternalService {
    /** Send Profile Management URL */
    public static final String TEMPLATE_PROFILE_SELECT_PATH = "/sendProfile/select";    // 발신프로필 조회

    /** Autowired Components */
    private final PlatformProperty platformProperty;
    private final WebClient kakaoBizTalkWebClient;

    @Override
    @CircuitBreaker(name = "default", fallbackMethod = "sendProfileExternalCallFailed")
    public ResponseEntity<? super BizTalkResponseDto> getSendProfileKey(@NotNull String sendProfileKey) {
        BizTalkResponseDto<SendProfileResponseDto> response =
                kakaoBizTalkWebClient.get().uri(
                        String.format(
                                "%s/%s",
                                getTemplateRequestUrl(platformProperty, TEMPLATE_PROFILE_SELECT_PATH, "v1"),
                                sendProfileKey))
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<BizTalkResponseDto<SendProfileResponseDto>>() {})
                        .block();
        log.info("send profile response = {}", response);

        if (!response.getCode().contains("200"))
            return ResponseDto.customFailedMessage(response.getCode(), response.getMessage());

        return ResponseEntity.ok(response);
    }

    /////////////////////////// private methods ///////////////////////////
    private ResponseEntity<ResponseDto> sendProfileExternalCallFailed(Throwable throwable) {
        log.error("BizMessageCenter API Call Failed Fallback: {}", throwable.getMessage());
        return ResponseDto.bizCenterCallFailedMessage();
    }
}
