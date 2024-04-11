package com.kep.portal.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class RestLogoutSuccessHandler implements LogoutSuccessHandler {
    
    @Resource
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null && authentication.getName() != null) {
            String username = authentication.getName();
            String sessionKey = "user:session:" + username;
            if (redisTemplate != null) {
                redisTemplate.delete(sessionKey);
                log.info("Redis session key deleted for user: {}", username);
            } else {
                log.error("Redis template is null, cannot delete session key for user: {}", username);
            }
        } else {
            log.info("Authentication object or username is null, skipping Redis session key deletion.");
        }
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", true);

        ApiResult<Map<String, Object>> result = ApiResult.<Map<String, Object>>builder()
                .code(ApiResultCode.succeed)
                .payload(resultMap)
                .build();

        objectMapper.writeValue(response.getWriter(), result);
        response.getWriter().flush();
        log.info("Logout successful.");
    }
}
