package com.foogui.boot.skeleton.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean("customThreadPool")
    public Executor customThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 当前机器的核心数
        int processors = Runtime.getRuntime().availableProcessors();

        executor.setCorePoolSize(processors);
        executor.setMaxPoolSize(processors * 2);
        executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.setThreadNamePrefix("custom-thread-");

        executor.initialize();
        return executor;
    }
}
