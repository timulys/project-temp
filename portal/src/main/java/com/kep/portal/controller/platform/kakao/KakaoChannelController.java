package com.kep.portal.controller.platform.kakao;

import com.kep.core.model.dto.platform.PlatformSubscribeDto;
import com.kep.core.model.dto.platform.kakao.KakaoChannelDto;
import com.kep.portal.service.platform.PlatformSubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.time.ZonedDateTime;

/**
 * 카카오 채널 이벤트
 */
@RestController
@RequestMapping("/api/v1/kakao")
@Slf4j
public class KakaoChannelController {

    @Resource
    private PlatformSubscribeService platformSubscribeService;

    /**
     * 채널 추가 / 차단
     * @param dto
     * dto.event=added 추가
     * dto.event=blocked 차단
     */
    @PostMapping("/subscribe")
    public void channel(@RequestBody KakaoChannelDto dto) {
        log.info("KAKAO , CHANNEL DTO:{}",dto);

        boolean enabled = "added".equals(dto.getEvent());

        ZonedDateTime modified = ZonedDateTime.parse(dto.getUpdatedAt());
        PlatformSubscribeDto subscribe = PlatformSubscribeDto.builder()
                .serviceId(dto.getChannelUuid())
                .platformUserId(dto.getAppUserId())
                .enabled(enabled)
                .modified(modified)
                .build();

        platformSubscribeService.store(subscribe);
    }
}
