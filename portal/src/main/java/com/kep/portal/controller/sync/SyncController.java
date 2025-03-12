package com.kep.portal.controller.sync;

import com.kep.portal.client.SyncClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class SyncController {
    /** Kakao Sync Platform Call Service **/
    private final SyncClient syncClient;

    /** Platform Kakao-Sync API **/
    @GetMapping("/getSync")
    public String getKakaoSync(@RequestParam String state) {
        log.info("Request Kakao-Sync  {}", state);
        ResponseEntity<String> result = syncClient.getSync(new HttpHeaders(), state);
        return "redirect:" + result.getBody();
    }

    /** Platform Authorized API **/
    @GetMapping("/authorized")
    public String authorized(@RequestParam(required = false) Map<String, String> requestParams) {
        log.info("Request Authorized : {}", requestParams);
        ResponseEntity<String> result = syncClient.authorized(new HttpHeaders(), requestParams);
        return "redirect:" + result.getBody();
    }
}
