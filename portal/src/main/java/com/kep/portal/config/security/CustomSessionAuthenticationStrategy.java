package com.kep.portal.config.security;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
/**
 * CustomSessionAuthenticationStrategy는 레디스 데이터 저장소를 사용하여 세션 관리
 * #동시 로그인을 방지
 *
 * @author   YO
 * @version  1.0
 * @since    2023.11.28
 * @modificationHistory
 *   2023.11.28 / YO   /  초기 버전 생성
 */
@Component
public class CustomSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    private final SessionRegistry sessionRegistry;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SessionRepository<? extends Session> sessionRepository;

    // 레디스 템플릿과 세션 레포지토리를 주입
    public CustomSessionAuthenticationStrategy(SessionRegistry sessionRegistry,
                                                RedisTemplate<String, Object> redisTemplate,
                                                SessionRepository<? extends Session> sessionRepository) {this.sessionRegistry = sessionRegistry;
        this.redisTemplate = redisTemplate;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        String username = authentication.getName();
        String currentSessionId = request.getSession().getId();
        
        if (isUserAlreadyLoggedIn(username, currentSessionId)) {
            throw new SessionAuthenticationException("이미 로그인된 계정입니다.");
        }

        sessionRegistry.registerNewSession(request.getSession().getId(), authentication.getPrincipal());
    }

    public boolean isUserAlreadyLoggedIn(String username, String currentSessionId) {
        Set<String> keys = redisTemplate.keys("session:portal:sessions:*");
        for (String key : keys) {
            Session session = sessionRepository.findById(key.split(":")[3]);
            if (session != null) {
                SecurityContext securityContext = session.getAttribute("SPRING_SECURITY_CONTEXT");
                if (securityContext != null) {
                    Authentication existingAuth = securityContext.getAuthentication();
                    if (existingAuth != null && username.equals(existingAuth.getName())) {
                        // 현재 세션 ID와 기존 세션 ID 비교
                        String existingSessionId = session.getId();
                        if (!currentSessionId.equals(existingSessionId)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
