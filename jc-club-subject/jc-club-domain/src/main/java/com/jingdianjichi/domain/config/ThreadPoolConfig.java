package com.jingdianjichi.domain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池的config管理
 */
@Configuration
public class ThreadPoolConfig {
    @Bean(name = "LabelThreadPool")
    public ThreadPoolExecutor getLabelThreadPool() {
        return new ThreadPoolExecutor(20, 100, 5,
                TimeUnit.MINUTES, new LinkedBlockingQueue<>(40), Executors.defaultThreadFactory()
                , new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
