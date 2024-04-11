package com.kep.portal.controller.sync;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kep.portal.model.dto.sync.SyncInfo;
import com.kep.portal.service.sync.SyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Deprecated
@RestController
@RequestMapping("/api/v1/sync")
public class SyncController {

    @Resource
    private SyncService syncService;

    /**
     * 싱크 콜백
     * @param code
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/callback")
    public SyncInfo syncCallback(@RequestParam("code") String code) throws JsonProcessingException {
        log.info("code = {}", code);
        String accessToken = syncService.getAccessToken(code);
        SyncInfo userInfo = syncService.getKakaoUserInfo(accessToken);
        return userInfo;
    }
}
