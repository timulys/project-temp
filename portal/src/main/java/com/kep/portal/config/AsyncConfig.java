package com.kep.portal.config;

import com.kep.portal.config.exception.AsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 비동기 쓰레드 설정
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {

        return new ThreadPoolTaskExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {

        return new AsyncExceptionHandler();
    }

    @Bean(name = "assignProducerExecutor")
    public Executor jobAssignProducerExecutorExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(30);
        executor.setMaxPoolSize(30);
//		executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("assignProducerExecutor");
        executor.initialize();

        return executor;
    }

    @Bean(name = "platformClientExecutor")
    public Executor jobPlatformClientExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(30);
        executor.setMaxPoolSize(30);
//		executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("platformClientExecutor");
        executor.initialize();

        return executor;
    }


    @Bean("eventTaskExecutor")
    public TaskExecutor threadPoolTaskExecutor() {

        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(30);
        executor.setMaxPoolSize(30);
//        executor.setTaskDecorator(new CustomTaskDecorator());
        executor.setThreadNamePrefix("eventTaskExecutor.");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
}
