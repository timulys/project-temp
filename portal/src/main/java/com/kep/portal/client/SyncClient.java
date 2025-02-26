package com.kep.portal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Platform Kakao-Sync & Authorized Service Call Feign Client
 */
@FeignClient(name = "platform-service", url = "${spring.cloud.discovery.client.simple.instances.platform-service[0].uri}")
public interface SyncClient {
    /** Kakao sync method **/
    @GetMapping("/auth/kakao-sync/getSync")
    ResponseEntity<String> getSync(@RequestHeader HttpHeaders httpHeaders, @RequestParam String state);

    @GetMapping("/auth/kakao-sync/authorized")
    ResponseEntity<String> authorized(@RequestHeader HttpHeaders httpHeaders, @RequestParam(required = false) Map<String, String> requestParams);
}
