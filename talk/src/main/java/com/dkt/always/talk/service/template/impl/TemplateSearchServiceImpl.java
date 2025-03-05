package com.dkt.always.talk.service.template.impl;

import com.dkt.always.talk.client.ChannelServiceClient;
import com.dkt.always.talk.client.MemberServiceClient;
import com.dkt.always.talk.entity.template.PlatformTemplate;
import com.dkt.always.talk.repository.template.TemplateSearchRepository;
import com.dkt.always.talk.service.template.TemplateSearchService;
import com.dkt.always.talk.utils.MessageSourceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.bizTalk.request.TemplateSearchRequestDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TemplateResponseDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TemplateSearchResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Template Search Inner Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateSearchServiceImpl implements TemplateSearchService {
    /** Autowired Components */
    private final TemplateSearchRepository templateSearchRepository;
    private final ObjectMapper objectMapper;

    /** Feign Clients */
    private final MemberServiceClient memberServiceClient;
    private final ChannelServiceClient channelServiceClient;

    /** Message Source Util **/
    private final MessageSourceUtil messageUtil;

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "default", fallbackMethod = "portalServiceCallFailed")
    public ResponseEntity<? super ApiResult<List<TemplateSearchResponseDto>>> getTemplateList(TemplateSearchRequestDto requestDto, Pageable pageable) {
        Page<PlatformTemplate> searchResult = templateSearchRepository.search(requestDto, pageable);
        List<TemplateSearchResponseDto> result = searchResult.stream()
                .map(this::makeTemplateSearchResponseDto).collect(Collectors.toList());

        return ResponseEntity.ok(ApiResult.<List<TemplateSearchResponseDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(result)
                .totalPage(searchResult.getTotalPages())
                .totalElement(searchResult.getTotalElements())
                .currentPage(searchResult.getNumber())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "default", fallbackMethod = "portalServiceCallFailed")
    public ResponseEntity<? super ApiResult<TemplateResponseDto>> getTemplate(Long id) throws Exception {
        PlatformTemplate entity = templateSearchRepository.findById(id).orElse(null);

        TemplateResponseDto responseDto = makeTemplateResponseDto(entity);
        responseDto.updateDetail(objectMapper.readValue(entity.getPayload(), KakaoBizMessageTemplatePayload.class));

        return ResponseEntity.ok(ApiResult.<TemplateResponseDto>builder()
                        .code(ApiResultCode.succeed)
                        .payload(responseDto)
                        .build());
    }

    /////////////////////////// private methods ///////////////////////////
    private TemplateResponseDto makeTemplateResponseDto(PlatformTemplate platformTemplate) {
        TemplateResponseDto responseDto = objectMapper.convertValue(platformTemplate, TemplateResponseDto.class);

        responseDto.updateCreator(memberServiceClient.getMember(platformTemplate.getCreator()).getPayload());
        if (!ObjectUtils.isEmpty(platformTemplate.getModifier())) {
            responseDto.updateModifier(memberServiceClient.getMember(platformTemplate.getModifier()).getPayload());
        }

        // Channel 정보 주입
        responseDto.updateChannel(channelServiceClient.getChannel(platformTemplate.getChannelId()).getPayload());

        return responseDto;
    }

    private TemplateSearchResponseDto makeTemplateSearchResponseDto(PlatformTemplate platformTemplate) {
        TemplateSearchResponseDto responseDto = objectMapper.convertValue(platformTemplate, TemplateSearchResponseDto.class);

        responseDto.updateCreator(memberServiceClient.getMember(platformTemplate.getCreator()).getPayload());
        if (!ObjectUtils.isEmpty(platformTemplate.getModifier())) {
            responseDto.updateModifier(memberServiceClient.getMember(platformTemplate.getModifier()).getPayload());
        }

        return responseDto;
    }

    /////////////////////////// circuit breaker methods ///////////////////////////
    private ResponseEntity<ResponseDto> portalServiceCallFailed(Throwable throwable) {
        log.error("Portal Service API Call Failed Fallback: {}", throwable.getMessage());
        return ResponseDto.bizCenterCallFailedMessage(messageUtil.getMessage("bzm_call_failed"));
    }
}
