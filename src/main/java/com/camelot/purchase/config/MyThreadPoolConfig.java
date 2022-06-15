package com.camelot.purchase.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolTaskExecutor是spring core包中的，
 * 而ThreadPoolExecutor是JDK中的JUC。
 * ThreadPoolTaskExecutor是对ThreadPoolExecutor进行了封装处理。
 * <p>
 * 线程池按以下行为执行任务
 * 1. 当线程数小于核心线程数时，创建线程。
 * 2. 当线程数大于等于核心线程数，且任务队列未满时，将任务放入任务队列。
 * 3. 当线程数大于等于核心线程数，且任务队列已满
 * - 若线程数小于最大线程数，创建线程
 * - 若线程数等于最大线程数，抛出异常，拒绝任务
 */
@Configuration
public class MyThreadPoolConfig {

    /**
     * 注入线程池
     *
     * @return
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("pur-thd-pol-%d")
                .build();
        return new MdcThreadPoolExecutor(MDC.getCopyOfContextMap(), 32,
                100, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(9999),
                namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
    }

}

