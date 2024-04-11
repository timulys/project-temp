package com.kep.portal.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * 비동기 쓰레드 예외 처리
 */
@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(
            Throwable throwable, Method method, Object... obj) {

        log.error("ASYNC EXCEPTION, METHOD: {}, {}", method.getName(), throwable.getLocalizedMessage());
        for (Object param : obj) {
            log.error("ASYNC EXCEPTION, METHOD: {}, {}", method.getName(), param);
        }
    }
}
