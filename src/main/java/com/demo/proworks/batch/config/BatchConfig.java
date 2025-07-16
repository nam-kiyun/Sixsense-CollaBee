package com.demo.proworks.batch.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 배치 처리 설정 클래스
 * 스케줄러 및 비동기 실행 환경 설정
 * 
 * @author Claude AI
 * @since 2025-01-15
 */
@Configuration
@EnableScheduling
@EnableAsync
public class BatchConfig {

    /**
     * 비동기 배치 처리를 위한 Thread Pool 설정
     */
    @Bean(name = "batchTaskExecutor")
    public Executor batchTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 코어 스레드 풀 크기
        executor.setCorePoolSize(2);
        
        // 최대 스레드 풀 크기
        executor.setMaxPoolSize(5);
        
        // 큐 용량 (대기 중인 태스크 수)
        executor.setQueueCapacity(100);
        
        // 스레드 이름 접두사
        executor.setThreadNamePrefix("KanbanBatch-");
        
        // 스레드 유지 시간 (초)
        executor.setKeepAliveSeconds(60);
        
        // 애플리케이션 종료 시 대기 중인 태스크 완료까지 기다리기
        executor.setWaitForTasksToCompleteOnShutdown(true);
        
        // 종료 대기 시간 (초)
        executor.setAwaitTerminationSeconds(60);
        
        // 거부된 태스크 처리 정책 (호출한 스레드에서 실행)
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        
        executor.initialize();
        
        System.out.println("배치 처리용 Thread Pool 설정 완료");
        System.out.println("- Core Pool Size: " + executor.getCorePoolSize());
        System.out.println("- Max Pool Size: " + executor.getMaxPoolSize());
        System.out.println("- Queue Capacity: " + executor.getQueueCapacity());
        
        return executor;
    }

    /**
     * 스케줄러 Thread Pool 설정
     */
    @Bean(name = "schedulerTaskExecutor")
    public Executor schedulerTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 스케줄러용으로 적은 스레드 사용
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("KanbanScheduler-");
        executor.setKeepAliveSeconds(30);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        
        executor.initialize();
        
        System.out.println("스케줄러용 Thread Pool 설정 완료");
        
        return executor;
    }
}